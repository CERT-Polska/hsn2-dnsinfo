package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class RSWhoIsParser extends AbstractRegExpWhoisParser {
	public RSWhoIsParser() {
		super();

		int blockNumber = 0;
		blocks.put(++blockNumber, Pattern.compile("domain name:(?>[\\s]*)(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(++blockNumber, Pattern.compile("(registrant:(.*?))[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(++blockNumber, Pattern.compile("(dns:(.*?))[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(++blockNumber, Pattern.compile("(administrative contact:(.*?))$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 0;
		Map<Pattern, String> map;

		map = new HashMap<>();
		map.put(Pattern.compile("domain status:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("registration date:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("modification date:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("expiration date:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("registrar:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		blockItems.put(++blockNumber, map);

		map = new HashMap<>();
		map.put(Pattern.compile("registrant:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("address:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("id number:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		blockItems.put(++blockNumber, map);

		map = new HashMap<>();
		map.put(Pattern.compile("dns:(?>[\\s]*)([^\\s]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(++blockNumber, map);

		map = new HashMap<>();
		map.put(Pattern.compile("administrative contact:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		map.put(Pattern.compile("technical contact:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		blockItems.put(++blockNumber, map);

		availableItem = Pattern.compile("Domain is not registered", Pattern.CASE_INSENSITIVE);
	}
}
