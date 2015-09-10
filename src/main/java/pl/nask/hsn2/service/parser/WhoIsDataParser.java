package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoIsDataParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WhoIsDataParser.class);
	
	private static Map<String, WhoIsParser> parsers = new HashMap<String, WhoIsParser>();
	
	static {
		parsers.put("whois.nic.it", new ITWhoIsParser());
	}
	
	public Map<String, String> parse(String whoisServer, String whoisData) {
		
		if (whoisServer == null || "".equals(whoisServer)) {
			LOGGER.error("WhoIs server must be provided: {}", whoisServer);
			return null;
		}
		
		if (whoisData == null || "".equals(whoisData)) {
			LOGGER.error("Whois data not provided: {}", whoisData);
			return null;
		}
		
		if (!parsers.containsKey(whoisServer)) {
			LOGGER.error("There is no data parser for this whois server: {}", whoisServer);
			return null;
		}
		
		// get parser for particulkar whois server
		WhoIsParser parser = parsers.get(whoisServer);
		return parser.parse(whoisData);
	}
}
