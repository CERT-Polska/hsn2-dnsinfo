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

package pl.nask.hsn2.service.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WhoisParserFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhoisParserFactory.class);

	private static final Map<String, WhoIsParser> PARSERS = new HashMap<String, WhoIsParser>();

	private WhoisParserFactory() {}

	public static WhoIsParser getParser(final String domain) {
		if (domain == null || "".equals(domain)) {
			throw new IllegalArgumentException("Domain cannot be empty.");
		}

		String zone = domain;
		while (zone.endsWith(".")) {
			zone = zone.substring(0, zone.length()-1);
		}

		if (zone == null || "".equals(zone)) {
			throw new IllegalArgumentException("Domain cannot be empty.");
		}

		int lastDotIndex = zone.lastIndexOf('.');
		if (lastDotIndex >= 0){
			zone = zone.substring(lastDotIndex + 1);
		}

		WhoIsParser parser = null;
		synchronized (PARSERS) {
			if (PARSERS.containsKey(zone)) {
				return PARSERS.get(zone);
			}
			parser = getOrCreateParser(domain, zone);
		}
		return parser;
	}

	private static WhoIsParser getOrCreateParser(String domain, String zone) {
		WhoIsParser parser = null;
		Class<? extends WhoIsParser> parserClass;
		String parserClassName = null;
		if (zone.startsWith("xn--")) {
			parserClassName = "pl.nask.hsn2.service.parser.idn." + zone.substring(4).toUpperCase() + "WhoIsParser";
		} else {
			parserClassName = "pl.nask.hsn2.service.parser." + zone.toUpperCase() + "WhoIsParser";
		}
		try {
			parserClass = Class.forName(parserClassName).asSubclass(WhoIsParser.class);
			parser = parserClass.newInstance();
			PARSERS.put(zone, parser);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Cannot find parser for domain {}", domain);
		} catch (InstantiationException e) {
			LOGGER.error("Cannot instantiate parser for domain {}", domain);
		} catch (IllegalAccessException e) {
			LOGGER.error("Illegal access during instantiation of the parser for domain {}", domain);
		}
		return parser;
	}

	public static Map<String, WhoIsParser> getParsersMap() {
		return Collections.unmodifiableMap(PARSERS);
	}

	public static void clearParsersMap() {
		synchronized (PARSERS) {
			PARSERS.clear();
		}
		LOGGER.debug("Parsers map has been cleared.");
	}
}
