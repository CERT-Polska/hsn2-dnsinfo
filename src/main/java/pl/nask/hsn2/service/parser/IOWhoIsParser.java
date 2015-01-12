package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class IOWhoIsParser extends AbstractRegExpWhoisParser {
	public IOWhoIsParser() {
		super();

		blocks.put(1, Pattern.compile("domain(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("status(?>[\\s]*):(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("expiry(?>[\\s]*):(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("ns (?>[\\d]+)(?>[\\s]*):(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("owner(?>[\\s]*):(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		blockItems.put(1, map);

		availableItem = Pattern.compile("is available for purchase", Pattern.CASE_INSENSITIVE);
	}
}
