package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class UKWhoIsParser extends AbstractRegExpWhoisParser {
	public UKWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("(registrant:(.*?))(?=registrant type)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(registrar:(.*?))(?=relevant dates)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("relevant dates:(.*?)(?=registration status)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("name servers:(.*?)(?=whois lookup made)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;

		map = new HashMap<>();
		map.put(Pattern.compile("registrant:(?>[\\s]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("registrar:(?>[\\s]*)(.+) \\[.+\\]$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("\\[tag = (.+)\\]$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:id");
		map.put(Pattern.compile("url:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:url");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("registered on:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("expiry date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("last updated:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("\\n(?>[\\x20\\t]+)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("This domain name has not been registered", Pattern.CASE_INSENSITIVE);
	}
}
