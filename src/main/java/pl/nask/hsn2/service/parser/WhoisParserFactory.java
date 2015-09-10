package pl.nask.hsn2.service.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoisParserFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhoisParserFactory.class);
	
	private static final Map<String, WhoIsParser> parsers = new HashMap<String, WhoIsParser>();
	
	public static final WhoIsParser getParser(final String domain) {
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
		synchronized (parsers) {
			if (parsers.containsKey(zone)) {
				return parsers.get(zone);
			}
			
			Class<? extends WhoIsParser> parserClass;
			String parserClassName = "pl.nask.hsn2.service.parser." + zone.toUpperCase() + "WhoIsParser";
			try {
				parserClass = Class.forName(parserClassName).asSubclass(WhoIsParser.class);
				parser = parserClass.newInstance();
				parsers.put(zone, parser);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Cannot find parser for domain {}", domain);
			} catch (InstantiationException e) {
				LOGGER.error("Cannot instantiate parser for domain {}", domain);
			} catch (IllegalAccessException e) {
				LOGGER.error("Illegal access during instantiation of the parser for domain {}", domain);
			}
		}
		return parser;
	}
	
	public static final Map<String, WhoIsParser> getParsersMap() {
		return Collections.unmodifiableMap(parsers);
	}
	
	public static final void clearParsersMap() {
		synchronized (parsers) {
			parsers.clear();
		}
		LOGGER.debug("Parsers map has been cleared.");
	}
}
