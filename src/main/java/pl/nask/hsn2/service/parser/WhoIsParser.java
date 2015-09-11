package pl.nask.hsn2.service.parser;

import java.util.Map;

public interface WhoIsParser {

	Map<String, String> parse(String whoisData);

	// is domain available means no whois data
	boolean isAvailable(String whoisData);
}
