package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class NZWhoIsParser extends AbstractRegExpWhoisParser {
	public NZWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("(domain_name:(?>[\\x20\\t]*)(.*?))(?=registrar_name)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(registrar_name:(?>[\\x20\\t]*)(.*?))(?=registrant_contact_name)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(registrant_contact_name:(?>[\\x20\\t]*)(.*?))(?=admin_contact_name)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(admin_contact_name:(?>[\\x20\\t]*)(.*?))(?=technical_contact_name)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(technical_contact_name:(?>[\\x20\\t]*)(.*?))(?=ns_name_01)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("(ns_name_01:(?>[\\x20\\t]*)(.*?))$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;

		map = new HashMap<>();
		map.put(Pattern.compile("^query_status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("^domain_dateregistered:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("^domain_datebilleduntil:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("^domain_datelastmodified:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("^domain_signed:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("^registrar_name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("^registrar_email:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:email");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("^registrant_contact_name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("^registrant_contact_address[0-9]:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("^registrant_contact_city:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:city");
		map.put(Pattern.compile("^registrant_contact_postalcode:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:zipcode");
		map.put(Pattern.compile("^registrant_contact_country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:country");
		map.put(Pattern.compile("^registrant_contact_email:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:email");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("^admin_contact_name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		map.put(Pattern.compile("^admin_contact_email:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:email");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("^technical_contact_name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		map.put(Pattern.compile("^technical_contact_email:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:email");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("^ns_name_0[0-9]:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("query_status: 220 Available", Pattern.CASE_INSENSITIVE);
	}
}
