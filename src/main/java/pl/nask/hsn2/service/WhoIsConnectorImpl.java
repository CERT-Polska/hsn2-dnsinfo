/*
 * Copyright (c) NASK
 * 
 * This file is part of HoneySpider Network 2.1.
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.net.whois.WhoisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WhoIsConnectorImpl implements WhoIsConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhoIsConnectorImpl.class);

	public static final String DEFAULT_WHOIS_SERVERS_FILE = "dnsinfo-whois-servers.txt";
	
	private Map<String,String> whoisServers = new HashMap<String, String>();

	private WhoisClient whoisClient = new WhoisClient(); 

	public WhoIsConnectorImpl() throws FileNotFoundException {
		this(DEFAULT_WHOIS_SERVERS_FILE);
	}
	
	public WhoIsConnectorImpl(String path) throws FileNotFoundException {
		super();
		loadServersList(path);
	}

	public final void loadServersList(String path) throws FileNotFoundException {

		if (!this.whoisServers.isEmpty()) {
			this.whoisServers.clear();
		}

		Scanner inFile = new Scanner(new File(path));
		int counter = 0;
		int lineCounter = 0;
		while(inFile.hasNext()){
			String line = inFile.nextLine().trim();
			lineCounter++;
			if (line.startsWith(";") || "".equals(line)) {
				//it's comment line or empty, skipping
				continue;
			}
			String[] tokens = line.split(" ");
			if (tokens.length < 2) {
				LOGGER.warn("Cannot parse whois servers file, line {}: {}", lineCounter, line);
			}
			this.whoisServers.put(tokens[0], tokens[1]);
			counter++;
		}
		inFile.close();
		LOGGER.info("Read {} servers from file: {}", counter, path);
	}

	public final String getWhoisServer(String domain) {
		String tmp = domain==null?null:domain.trim();
		if (domain == null || "".equals(tmp)) {
			LOGGER.error("It's stupid to looking for whois server for empty domain.");
			return null;
		}
		LOGGER.debug("Looking whois server for domain: {}", domain);

		for (String key : this.whoisServers.keySet()) {
			if (tmp.endsWith("." + key)) {
				String response = this.whoisServers.get(key);
				LOGGER.debug("Found whois server {} for domain {}", response, domain);
				return response;
			}
		}
		LOGGER.warn("Cannot find whois server for domain: {}", domain);
		return null;
	}
	
	@Override
	public final String getWhoisData(String domain) {
		
		String whoisServer = getWhoisServer(domain);
	
		if (whoisServer == null) {
			return null;
		}
		
		String query;
		if (whoisServer.equals("whois.verisign-grs.com")) {
			query = "=" + domain;
		} else {
			query = domain;
		}
		
		StringBuilder result = new StringBuilder("");
		try {
			whoisClient.connect(whoisServer);
			String whoisData1 = whoisClient.query(query);
			result.append(whoisData1);
			whoisClient.disconnect();
			return result.toString();
		} catch (IOException e) {
			LOGGER.error("Cannot get whois data for domain: {}", domain);
			LOGGER.error("IOException catched", e);
		}
		return null;
	}
}
