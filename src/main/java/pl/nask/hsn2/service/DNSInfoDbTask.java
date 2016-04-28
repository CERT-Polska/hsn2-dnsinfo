/*
 * Copyright (c) NASK
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.nask.hsn2.ParameterException;
import pl.nask.hsn2.ResourceException;
import pl.nask.hsn2.StorageException;
import pl.nask.hsn2.TaskContext;
import pl.nask.hsn2.wrappers.ObjectDataWrapper;
import pl.nask.hsn2.wrappers.ParametersWrapper;

public class DNSInfoDbTask extends DNSInfoTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(DNSInfoDbTask.class);

	// workflow parameters
	private static final String WFL_KEY_DB_KEYS_LIST = "db_keys_list";
	private static final String WFL_KEY_DB_TABLE_NAME = "db_table_name";

	//class variables
	private final MysqlAdapter mysqlAdapter;
	private final List<String> dbKeysList = new ArrayList<String>();

	public DNSInfoDbTask(
			final TaskContext jobContext,
			final ParametersWrapper parameters, final ObjectDataWrapper data,
			final String cmdZonesPath, final String cmdWhoisServersPath,
			final String cmdMysqlUrl, final String cmdMysqlDb,
			final String cmdMysqlUser, final String cmdMysqlPasswd)
			throws ParameterException {
	
		super(jobContext, parameters, data, cmdZonesPath, cmdWhoisServersPath);
		
		parseDbKeysList(this.dbKeysList, parameters.get(WFL_KEY_DB_KEYS_LIST, null));

		this.mysqlAdapter = new MysqlAdapter(
					cmdMysqlUrl, cmdMysqlDb,
					cmdMysqlUser, cmdMysqlPasswd);

		this.mysqlAdapter.setupInsertStatement(
					parameters.get(WFL_KEY_DB_TABLE_NAME, null),
					this.dbKeysList);
	}

	protected final void processWhoisData(String rootDomain, String whoisData) throws StorageException, ResourceException {
		
		//TODO: check if domain exists in DB, update info if needed
		
		Map<String, String> result = parseWhoisData(rootDomain, whoisData);
				
		storeWhoisEntity(rootDomain, whoisData, result);
	}

	private void storeWhoisEntity(final String rootDomain, final String fullData, final Map<String, String> result) throws ResourceException {
		if (result == null) {
			return;
		}
		
		final Iterator<String> iterator = this.dbKeysList.iterator();
		this.mysqlAdapter.persistWhoisInfo(rootDomain, new Callable<String>() {
			@Override
			public String call() {
				String key = iterator.next();
				String value = null;
				if (key.startsWith("*")) {
					key = key.substring(1);
					value = fullData;
				} else {
					value = result.get(key);
				}
				LOGGER.debug("Setting up key {} for value {}", key, value);
				return value;
			}
		});
	}
	
	private static void parseDbKeysList(List<String> list, String string) {
		if (string == null || "".equals(string)) {
			return;
		}
		
		String unified = string.replaceAll("[\\n\\s\\t;]+", " ");
		LOGGER.debug("keysListString unified to: {}", unified);
		
		for (String line : unified.split(" ")) {
			list.add(line);
		}
	}
}