package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class SEWhoIsParser extends AbstractRegExpWhoisParser {
	public SEWhoIsParser() {
		super();

		blocks.put(1, Pattern.compile("state:(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("holder:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("admin\\-c:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("tech\\-c:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("modified:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("expires:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("nserver:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("dnssec:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		blockItems.put(1, map);

		availableItem = Pattern.compile("not found", Pattern.CASE_INSENSITIVE);
	}
}
