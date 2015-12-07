package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRegExpWhoisParser implements WhoIsParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegExpWhoisParser.class);

	protected final Map<Integer, Pattern> blocks = new HashMap<Integer, Pattern>();
	protected final Map<Integer, Map<Pattern, String>> blockItems = new HashMap<Integer, Map<Pattern, String>>();

	protected Pattern availableItem = null;

	@Override
	public final Map<String, String> parse(String whoisData) {

		if (whoisData == null || "".equals(whoisData)) {
			LOGGER.error("Whois data cannot be empty or null.");
			return null;
		}

		Map<String, String> results = new HashMap<String, String>();

		if (isAvailable(whoisData)) {
			LOGGER.debug("Domain has no whois data.");
			return results;
		}

		for (Integer blockNum : blocks.keySet()) {
			Matcher blockMatcher = blocks.get(blockNum).matcher(whoisData);
			if (!blockMatcher.find()) {
				LOGGER.debug("Block {} cannot be matched.", blockNum);
				continue;
			}

			Map<Pattern, String> items = blockItems.get(blockNum);
			if (items == null) {
				LOGGER.error("There is no patterns for this block: {}", blockNum);
				continue;
			}

			for (Map.Entry<Pattern, String> entry : items.entrySet()) {
				Matcher itemMacher = entry.getKey().matcher(blockMatcher.group(1));
				while (itemMacher.find()) {
					if (results.containsKey(entry.getValue())) {
						results.put(entry.getValue(), results.get(entry.getValue()) + ";" + itemMacher.group(1));
					} else {
						results.put(entry.getValue(), itemMacher.group(1));
					}
				}
			}
		}

		processAfterParse(whoisData, results);

		return results;
	}

	@Override
	public final boolean isAvailable(String whoisData) {
		if (availableItem == null) {
			return true;
		}
		Matcher m = availableItem.matcher(whoisData);
		if (m.find()) {
			return true;
		}
		return false;
	}

	protected void processAfterParse(String whoisData, Map<String, String> results) {
		// optional processing specific for a zone
	}
}
