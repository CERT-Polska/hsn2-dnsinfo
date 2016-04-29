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

package pl.nask.hsn2.service.extractors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZonesBasedDomainExtractor implements DomainExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZonesBasedDomainExtractor.class);

	public static final String DEFAULT_ZONES_FILE = "zones.txt";

	private List<String> zones = new ArrayList<String>();

	public ZonesBasedDomainExtractor() throws FileNotFoundException {
		this(DEFAULT_ZONES_FILE);
	}

	public ZonesBasedDomainExtractor(String zonesPath) throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(zonesPath));
		int counter = 0;
		while(inFile.hasNext()){
			zones.add(inFile.nextLine().trim());
			counter++;
		}
		inFile.close();
		LOGGER.info("Read {} zones from file: {}", counter, zonesPath);
	}

	@Override
	public final String getDomain(String hostname) {

		if (hostname == null || "".equals(hostname)) {
			LOGGER.error("No valid domain: {}", hostname);
			return null;
		}

		LOGGER.debug("Domain is: {}", hostname);

		for (String zone : this.zones) {

			LOGGER.debug("Checking zone: {}", zone);

			if (hostname.equals(zone)) {
				LOGGER.error("Domain equals to zone: {}", hostname);
				return null;
			}

			if (hostname.endsWith("." + zone)) {

				String tmp = hostname.substring(0, hostname.lastIndexOf(zone)-1);

				int lastDotIndex = tmp.lastIndexOf('.');
				if (lastDotIndex >= 0) {
					tmp = tmp.substring(tmp.lastIndexOf('.')+1);
				}

				tmp = tmp + "." + zone;
				LOGGER.debug("Final domain: {}", tmp);

				return tmp;
			}
		}

		LOGGER.error("Cannot determine root domain for: {}", hostname);
		return null;
	}

}
