package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class WSWhoIsParser extends AbstractRegExpWhoisParser {
	public WSWhoIsParser() {
		super();

		blocks.put(1, Pattern.compile( ".*domain name:(?>[\\x20\\t]*)(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("Updated Date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"changed");
		map.put(Pattern.compile("Creation Date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"created");
		map.put(Pattern.compile("Registrar Registration Expiration Date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"expires");
		map.put(Pattern.compile("Registrar URL:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:url");
		map.put(Pattern.compile("Registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:name");
		map.put(Pattern.compile("Registrar IANA ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:id");
		map.put(Pattern.compile("Domain Status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"status");
		map.put(Pattern.compile("Name Server:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"nameservers");
		map.put(Pattern.compile("DNSSEC:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"dnssec");

		map.put(Pattern.compile("Registry Registrant ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:handle");
		map.put(Pattern.compile("Registrant Name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:name");
		map.put(Pattern.compile("Registrant Organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:organization");
		map.put(Pattern.compile("Registry Admin ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:handle");
		map.put(Pattern.compile("Admin Name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:name");
		map.put(Pattern.compile("Admin Organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:organization");
		map.put(Pattern.compile("Registry Tech ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:handle");
		map.put(Pattern.compile("Tech Name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:name");
		map.put(Pattern.compile("Tech Organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:organization");blockItems.put(1, map);

		availableItem = Pattern.compile(".*No match for.*", Pattern.CASE_INSENSITIVE);
	}
}
