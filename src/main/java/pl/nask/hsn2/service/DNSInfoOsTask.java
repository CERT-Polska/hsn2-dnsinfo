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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.nask.hsn2.ParameterException;
import pl.nask.hsn2.ResourceException;
import pl.nask.hsn2.StorageException;
import pl.nask.hsn2.TaskContext;
import pl.nask.hsn2.wrappers.ObjectDataWrapper;
import pl.nask.hsn2.wrappers.ParametersWrapper;

public class DNSInfoOsTask extends DNSInfoTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(DNSInfoOsTask.class);

	// workflow parameters
	private static final String WFL_KEY_WHOIS_DATA_KEY = "whois_data_key";
	private static final String WFL_KEY_KEYS_MAP = "keys_map";
	
	//class variables
	private final String fullWhoisDataKey;
	private final Map<String, String> osKeysMap = new HashMap<String, String>();

	public DNSInfoOsTask(
			final TaskContext jobContext,
			final ParametersWrapper parameters, final ObjectDataWrapper data,
			final String cmdZonesPath, final String cmdWhoisServersPath)
			throws ParameterException {

		super(jobContext, parameters, data, cmdZonesPath, cmdWhoisServersPath);

		this.fullWhoisDataKey = parameters.get(WFL_KEY_WHOIS_DATA_KEY, null);
		
		parseOsKeyMaps(this.osKeysMap, parameters.get(WFL_KEY_KEYS_MAP, null));
	}

	protected final void processWhoisData(String rootDomain, String whoisData) throws StorageException, ResourceException {

		saveFullWhoisIfNeeded(whoisData);
		
		Map<String, String> result = parseWhoisData(rootDomain, whoisData);
		mapOsKeys(result);
	}

	private void saveFullWhoisIfNeeded(String whoisData) throws StorageException, ResourceException {
		if (fullWhoisDataKey != null) {
			try {
				long whoisDataRefId = jobContext.saveInDataStore(IOUtils.toInputStream(whoisData, "UTF-8"));
				jobContext.addReference(fullWhoisDataKey, whoisDataRefId);
			} catch (IOException e) {
				LOGGER.error("Cannot save whois data into DataStore.", e);
			}
		}
	}
	
	private void mapOsKeys(Map<String, String> result) {
		if (result == null) {
			return;
		}
		for (String key : this.osKeysMap.keySet()) {
			String value = result.get(key);
			if (value != null) {
				LOGGER.debug("Setting up ObjectStore key {} with value {}",
						this.osKeysMap.get(key), value);
				jobContext.addAttribute(this.osKeysMap.get(key), value);
			} else {
				LOGGER.debug("There is no value for key {} attribute will not be creatd in object store.", key);
			}
		}
	}
	
	private static void parseOsKeyMaps(Map<String, String> map, String string) {
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
		}
	}
}