package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class EEWhoIsParser extends AbstractRegExpWhoisParser {
	public EEWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("domain:(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("registrant:(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("administrative contact:(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("technical contact:(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("registrar:(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("name servers:(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;

		map = new HashMap<>();
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("registered:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("changed:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("expire:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("nserver:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("domain not found", Pattern.CASE_INSENSITIVE);
	}
}
