package pl.nask.hsn2.service.parser;

import java.util.Map;

public interface WhoIsParser {

	public Map<String, String> parse(String whoisData);

	// is domain available means no whois data
	public boolean isAvailable(String whoisData);
}
