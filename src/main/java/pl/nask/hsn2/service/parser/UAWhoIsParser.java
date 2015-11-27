package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UAWhoIsParser extends AbstractRegExpWhoisParser {
	public UAWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("(domain:(?>[\\x20\\t]*)(.*?))(?=\\% registrar)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("\\% registrar:(.*?)(?=\\% registrant)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("\\% registrant:(.*?)(?=\\% administrative contacts)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("\\% administrative contacts:(.*?)(?=(\\% technical contacts|\\% query time))", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("\\% technical contacts:(.*?)(?=\\% query time)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map;
		blockNumber = 1;

		map = new HashMap<>();
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("nserver:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("modified:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("expires:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:id");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("contact-id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("person:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:organization");
		map.put(Pattern.compile("address:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("e\\-mail:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:email");
		map.put(Pattern.compile("country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:country");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("contact-id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("person:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:organization");
		map.put(Pattern.compile("address:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:address");
		map.put(Pattern.compile("e\\-mail:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:email");
		map.put(Pattern.compile("country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:country");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("contact-id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("person:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:organization");
		map.put(Pattern.compile("address:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");
		map.put(Pattern.compile("e\\-mail:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:email");
		map.put(Pattern.compile("country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:country");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile(".*No entries found for.*", Pattern.CASE_INSENSITIVE);
	}
}
