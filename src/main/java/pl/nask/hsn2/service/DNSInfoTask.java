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
import java.util.Map;

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

public abstract class DNSInfoTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(DNSInfoTask.class);

	// workflow parameters
	private static final String WFL_KEY_COLLECT_STATS = "collect_stats";
	private static final String WFL_KEY_URL_DOMAIN_KEY = "url_domain_key";
	private static final String WFL_KEY_CACHE_ON = "cache_on";
	private static final String WFL_KEY_CACHE_TIME = "cache_time";
	private static final String WFL_KEY_CACHE_LIMIT = "cache_limit";
	
	
	// object store attributes
	private final String urlDomain;

	//class variables
	protected final TaskContext jobContext;
	private final DomainExtractor extractor;
	private final WhoIsConnector whoisConnector;
	private final String urlDomainKey;
	private final boolean collectStats;

	public DNSInfoTask(
			final TaskContext jobContext,
			final ParametersWrapper parameters, final ObjectDataWrapper data,
			final String cmdZonesPath, final String cmdWhoisServersPath)
			throws ParameterException {

		this.jobContext = jobContext;

		this.collectStats = parameters.getBoolean(WFL_KEY_COLLECT_STATS, false);
		this.urlDomainKey = parameters.get(WFL_KEY_URL_DOMAIN_KEY, "url_domain");
		this.urlDomain = data.getString(urlDomainKey);
		
		try {
			this.extractor = new ZonesBasedDomainExtractor(cmdZonesPath);
		} catch (FileNotFoundException e) {
			throw new ParameterException("ZonesBasedDomainExtractor cannot be initialized.", e);
		}
		
		try {
			if (parameters.getBoolean(WFL_KEY_CACHE_ON, true)) {
				this.whoisConnector = new CachedWhoIsConnector(
						new WhoIsConnectorImpl(cmdWhoisServersPath),
							parameters.getInt(WFL_KEY_CACHE_TIME, 0),
							parameters.getInt(WFL_KEY_CACHE_LIMIT, 0));
			} else {
				this.whoisConnector = new WhoIsConnectorImpl(cmdWhoisServersPath);
			}
		} catch (FileNotFoundException e) {
			throw new ParameterException("WhoIsConnector cannot be initialized.", e);
		}
	}

	public final boolean takesMuchTime() {
		return false;
	}

	protected final Map<String, String> parseWhoisData(String rootDomain, String whoisData) {
		WhoIsParser parser = WhoisParserFactory.getParser(rootDomain);
		if (parser == null) {
			LOGGER.error("Cannot find whois parser for domain: {}", this.urlDomain);
			return null;
		}

		Map<String, String> result = parser.parse(whoisData);
		if (result == null || result.isEmpty()) {
			LOGGER.error("Cannot parse whois data for domain: {}", this.urlDomain);
			return null;
		}
		return result;
	}

	public final void process() throws ParameterException, ResourceException,
			StorageException, InputDataException {

		if (urlDomain == null || "".equals(urlDomain)) {
			LOGGER.warn("No domain name to process, verify the key in object-store: {}", urlDomainKey);
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

		processWhoisData(rootDomain, whoisData);
		
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

	protected abstract void processWhoisData(String rootDomain, String whoisData) throws StorageException, ResourceException;
}