package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ATWhoIsParser extends AbstractRegExpWhoisParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(ATWhoIsParser.class);

	private final Pattern namePattern = Pattern.compile("personname:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	public ATWhoIsParser() {
		super();

		blocks.put(1, Pattern.compile("domain:(?>[\\x20\\t]*)(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("registrant:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("admin\\-c:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("tech\\-c:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("nserver:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("changed:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("dnssec:(?>[\\x20\\t]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(1, map);

		availableItem = Pattern.compile("\\% nothing found", Pattern.CASE_INSENSITIVE);
	}

	@Override
	protected void processAfterParse(String whoisData, Map<String, String> results) {
		String ownerHandles = results.get("contacts:owner:handle");
		String adminHandles = results.get("contacts:admin:handle");
		String techHandles = results.get("contacts:tech:handle");

		String ownerName = matchName(ownerHandles, whoisData);
		if (ownerName != null) {
			results.put("contacts:owner:name", ownerName);
		}

		String adminName = matchName(adminHandles, whoisData);
		if (adminName != null) {
			results.put("contacts:admin:name", adminName);
		}

		String techName = matchName(techHandles, whoisData);
		if (techName != null) {
			results.put("contacts:tech:name", techName);
		}
	}

	private String matchName(String handles, String whoisData) {
		StringBuilder result = new StringBuilder();
		boolean found = false;

		for (String handle : handles.split(";")) {
			Pattern pattern = Pattern.compile("(personname:(((?![\\n]{2}).)*?)" + handle + "(((?![\\n]{2}).)*?))(?=source)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher blockMatcher = pattern.matcher(whoisData);
			if (!blockMatcher.find()) {
				LOGGER.error("Block {} cannot be matched.", handle);
				return null;
			}

			Matcher itemMacher = namePattern.matcher(blockMatcher.group(1));
			while (itemMacher.find()) {
				if (found) {
					result.append(";").append(itemMacher.group(1));
				} else {
					result.append(itemMacher.group(1));
					found = true;
				}
			}
		}

		return result.toString();
	}
}
