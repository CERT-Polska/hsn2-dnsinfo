package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class KZWhoIsParser extends AbstractRegExpWhoisParser {
	public KZWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("Organization Using Domain Name(.*?)(?=Administrative Contact\\/Agent)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("Administrative Contact\\/Agent(.*?)(?=Nameserver in listed order)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("Nameserver in listed order(.*?)(?=Domain created)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(Domain created(.*?))$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map;
		blockNumber = 1;

		map = new HashMap<>();
		map.put(Pattern.compile("^name(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("^organization name(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:organization");
		map.put(Pattern.compile("^street address(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("^city(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:city");
		map.put(Pattern.compile("^country(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:country");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("^nic handle(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("^name(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		map.put(Pattern.compile("^email address(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:email");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("^(?>Primary|Secondary) server(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("^(?>Primary|Secondary) ip address(?>[\\.]*)(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "ips");
		blockItems.put(blockNumber++, map);

		map = new HashMap<>();
		map.put(Pattern.compile("^Domain created(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("^Last modified(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("^Domain status(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("^Current Registar(?>[\\x20\\t]*):(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:id");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("Nothing found for this query", Pattern.CASE_INSENSITIVE);
	}
}
