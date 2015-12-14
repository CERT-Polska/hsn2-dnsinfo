package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class CNWhoIsParser extends AbstractRegExpWhoisParser {
	public CNWhoIsParser() {
		super();

		blocks.put(1, Pattern.compile("domain name:(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("Domain Status:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("Registrant ID:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("Registrant:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("Sponsoring Registrar:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("Name Server:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("Registration Time:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("Expiration Time:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("DNSSEC:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(1, map);

		availableItem = Pattern.compile("No matching record", Pattern.CASE_INSENSITIVE);
	}
}
