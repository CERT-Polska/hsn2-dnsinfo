package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class IMWhoIsParser extends AbstractRegExpWhoisParser {
	public IMWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("domain managers\\n(.*?)(?=domain owners \\/ registrant)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("domain owners \\/ registrant\\n(.*?)(?=administrative contact)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("administrative contact\\n(.*?)(?=billing contact)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("technical contact\\n(.*?)(?=domain details)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("domain details\\n(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("address\\n(.+)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:owner:address");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		map.put(Pattern.compile("address\\n(.+)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:admin:address");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		map.put(Pattern.compile("address\\n(.+)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:tech:address");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("expiry date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("name server:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("was not found", Pattern.CASE_INSENSITIVE);
	}
}
