package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class CHWhoIsParser extends AbstractRegExpWhoisParser {
	public CHWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("domain name:\\n(.*?)(?>$|name servers)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("name servers:(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map;
		blockNumber = 1;

		map = new HashMap<>();
		map.put(Pattern.compile("holder of domain name:\\n(.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("technical contact:\\n(.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		map.put(Pattern.compile("registrar:\\n(.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("first registration date:\\n(.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("dnssec:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("\\n(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("\\n(?>[\\x20\\t]*)(.+)(?>[\\x20\\t]*)\\[.+\\]$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("\\n(?>[\\x20\\t]*).+(?>[\\x20\\t]*)\\[(.+)\\]$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "ips");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("We do not have an entry in our database matching your query", Pattern.CASE_INSENSITIVE);
	}
}
