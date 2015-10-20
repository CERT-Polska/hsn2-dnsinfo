package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class ROWhoIsParser extends AbstractRegExpWhoisParser {

	public ROWhoIsParser() {
		super();
		blocks.put(1,
				Pattern.compile(
						"Domain Name:(?>[\\x20\\t]*)(.*?)$",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		Map<Pattern, String> map;
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile(".*Registered On:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile(".*Registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile(".*Nameserver:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile(".*Domain Status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		blockItems.put(1, map);

		availableItem = Pattern.compile(".*No entries found for the selected source.*", Pattern.CASE_INSENSITIVE);
	}
}
