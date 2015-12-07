package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAfnicWhoIsParser extends AbstractRegExpWhoisParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAfnicWhoIsParser.class);

	private final Pattern namePattern = Pattern.compile("contact:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	public AbstractAfnicWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("domain:(?>[\\x20\\t]*)(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("ns\\-list:(?>[\\x20\\t]*)(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;

		map = new HashMap<>();
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("holder\\-c:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("admin\\-c:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("tech\\-c:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("expiry date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("last\\-update:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("nserver:(?>[\\x20\\t]*)([^\\s]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("No entries found in the AFNIC Database", Pattern.CASE_INSENSITIVE);
	}

	@Override
	protected final void processAfterParse(String whoisData, Map<String, String> results) {
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

	private final String matchName(String handles, String whoisData) {
		String result = null;

		for (String handle : handles.split(";")) {
			Pattern pattern = Pattern.compile("nic\\-hdl:(?>[\\x20\\t]*)(?=" + handle + ")(.*?)[\\n]{2}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Matcher blockMatcher = pattern.matcher(whoisData);
			if (!blockMatcher.find()) {
				LOGGER.error("Block {} cannot be matched.", handle);
				return null;
			}

			Matcher itemMacher = namePattern.matcher(blockMatcher.group(1));
			while (itemMacher.find()) {
				if (result != null) {
					result = result + ";" + itemMacher.group(1);
				} else {
					result = itemMacher.group(1);
				}
			}
		}

		return result;
	}
}
