/*
 * Copyright (c) NASK, NCSC
 *
 * This file is part of HoneySpider Network 2.0.
 *
 * This is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.nask.hsn2.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.nask.hsn2.InputDataException;
import pl.nask.hsn2.ParameterException;
import pl.nask.hsn2.ResourceException;
import pl.nask.hsn2.StorageException;
import pl.nask.hsn2.TaskContext;
import pl.nask.hsn2.service.extractors.DomainExtractor;
import pl.nask.hsn2.service.extractors.ZonesBasedDomainExtractor;
import pl.nask.hsn2.service.parser.WhoIsParser;
import pl.nask.hsn2.service.parser.WhoisParserFactory;
import pl.nask.hsn2.task.Task;
import pl.nask.hsn2.wrappers.ObjectDataWrapper;
import pl.nask.hsn2.wrappers.ParametersWrapper;

public class DNSInfoTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(DNSInfoTask.class);

	private final DomainExtractor extractor;
	private final TaskContext jobContext;

	// service level configuration
	private final String zonesFile;
	private final String whoisServersList;

	// workflow parameters
	private final String urlDomainKey;
	private final boolean collectStats;
	private final String fullWhoisDataKey;
	private final String keysMapString;
	private final boolean cacheOn;
	private final int cacheTime;
	private final int cacheLimit;
	
	// object store attributes
	private final String urlDomain;

	//class variables
	private final WhoIsConnector whoisConnector;
	private final Map<String, String> keysMap = new HashMap<String, String>();

	public DNSInfoTask(
			final TaskContext jobContext,
			final ParametersWrapper parameters, final ObjectDataWrapper data,
			final String cmdZonesPath, final String cmdWhoisServersPath)
			throws ParameterException {

		this.jobContext = jobContext;

		this.zonesFile = cmdZonesPath;
		this.whoisServersList = cmdWhoisServersPath;

		this.collectStats = parameters.getBoolean("collect_stats", false);
		this.urlDomainKey = parameters.get("url_domain_key", "url_domain");
		this.urlDomain = data.getString(urlDomainKey);
		this.fullWhoisDataKey = parameters.get("whois_data_key", null);
		this.keysMapString = parameters.get("keys_map", null);
		this.cacheOn = parameters.getBoolean("cache_on", true);
		this.cacheTime = parameters.getInt("cache_time", 0);
		this.cacheLimit = parameters.getInt("cache_limit", 0);
		
		try {
			this.extractor = new ZonesBasedDomainExtractor(zonesFile);
		} catch (FileNotFoundException e) {
			throw new ParameterException("ZonesBasedDomainExtractor cannot be initialized.", e);
		}
		
		try {
			if (this.cacheOn) {
				this.whoisConnector = new CachedWhoIsConnector(
						new WhoIsConnectorImpl(whoisServersList),
						this.cacheTime, this.cacheLimit);
			} else {
				this.whoisConnector = new WhoIsConnectorImpl(whoisServersList);
			}
		} catch (FileNotFoundException e) {
			throw new ParameterException("WhoIsConnector cannot be initialized.", e);
		}
		
		parseKeyMaps(this.keysMap, this.keysMapString);
	}

	public final boolean takesMuchTime() {
		return false;
	}

	public final void process() throws ParameterException, ResourceException, StorageException, InputDataException {
		if (urlDomain == null || "".equals(urlDomain)) {
			LOGGER.warn("no domain name to process, verify the key in object-store: {}", urlDomainKey);
			return;
		}

		logStart();

		String rootDomain = extractor.getDomain(urlDomain);
		if (rootDomain == null) {
			logEnd();
			return;
		}
			
		String whoisData = whoisConnector.getWhoisData(rootDomain);
		if (whoisData == null) {
			LOGGER.error("Cannot get whois data for domain: {}", this.urlDomain);
			logEnd();
			return;
		}
		LOGGER.debug("Whois data: {}", whoisData);

		//save full whoisData if needed
		if (fullWhoisDataKey != null) {
				try {
					long whoisDataRefId = jobContext.saveInDataStore(IOUtils.toInputStream(whoisData, "UTF-8"));
					jobContext.addReference(fullWhoisDataKey, whoisDataRefId);
				} catch (IOException e) {
					LOGGER.error("Cannot save whois data into DataStore.", e);
				}
		}
		
		WhoIsParser parser = WhoisParserFactory.getParser(rootDomain);
		if (parser == null) {
			LOGGER.error("Cannot find whois parser for domain: {}", this.urlDomain);
			logEnd();
			return;
		}

		Map<String, String> result = parser.parse(whoisData);
		if (result == null || result.isEmpty()) {
			LOGGER.error("Cannot parse whois data for domain: {}", this.urlDomain);
			logEnd();
			return;
		}

		//map keys
		for (String key : this.keysMap.keySet()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Setting up ObjectStore key {} with value {}",
						this.keysMap.get(key), result.get(key));
			}
			jobContext.addAttribute(this.keysMap.get(key), result.get(key));
		}
		logEnd();
	}

	private void logEnd() {
		if (collectStats) {
			jobContext.addTimeAttribute("dns_info_time_end",
					System.currentTimeMillis());
		}
	}

	private void logStart() {
		if (collectStats) {
			jobContext.addTimeAttribute("dns_info_time_start",
					System.currentTimeMillis());
		}
	}

	public static final void parseKeyMaps(Map<String, String> map, String string) {
		if (string == null || "".equals(string)) {
			return;
		}
		
		String unified = string.replaceAll("[\\n\\s\\t;]+", " ");
		LOGGER.debug("keysMapString unified to: {}", unified);
		
		for (String line : unified.split(" ")) {
			String[] mapping = line.split("->");
			if (mapping == null || mapping.length != 2) {
				LOGGER.error("Problem with parsing keys map: {}. Ignoring.", line);
				continue;
			}
			map.put(mapping[0], mapping[1]);
		};
	}
}