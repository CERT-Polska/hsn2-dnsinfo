package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class PTWhoIsParser extends AbstractRegExpWhoisParser {
	public PTWhoIsParser() {
		super();

		blocks.put(1, Pattern.compile("nome de domínio(.*)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("Creation Date \\(dd/mm/yyyy\\):(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("Expiration Date \\(dd/mm/yyyy\\):(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("Status:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");

		map.put(Pattern.compile("Registrant(?>[\\s\\n\\r]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("Tech Contact(?>[\\s\\n\\r]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");

		map.put(Pattern.compile("NS(?>[\\s]*)([^\\s]+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");

		blockItems.put(1, map);

		availableItem = Pattern.compile("no match", Pattern.CASE_INSENSITIVE);
	}
}
