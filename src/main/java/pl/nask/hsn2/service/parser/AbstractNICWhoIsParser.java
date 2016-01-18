package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class AbstractNICWhoIsParser extends AbstractRegExpWhoisParser {

	public AbstractNICWhoIsParser() {
		super();
		blocks.put(1,
				Pattern.compile(
						".*domain name:(?>[\\x20\\t]*)(.*?)$",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		Map<Pattern, String> map;
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("whois server:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "whoisserver");
		map.put(Pattern.compile("sponsoring registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:name");
		map.put(Pattern.compile("sponsoring registrar IANA ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:id");
		map.put(Pattern.compile("referral url:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:url");
		map.put(Pattern.compile("creation date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"created");
		map.put(Pattern.compile("registry expiry date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"expires");
		map.put(Pattern.compile("updated date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"changed");
		map.put(Pattern.compile("name server:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"nameservers");
		map.put(Pattern.compile("dnssec:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"dnssec");
		map.put(Pattern.compile("domain status:(?>[\\x20\\t]*)([^\\s]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"status");

		map.put(Pattern.compile("registrant name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:name");
		map.put(Pattern.compile("registrant organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:organization");
		map.put(Pattern.compile("registrant id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:handle");
		map.put(Pattern.compile("registrant street:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:address");
		map.put(Pattern.compile("registrant city:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:address");
		map.put(Pattern.compile("registrant state/province:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:address");
		map.put(Pattern.compile("registrant postal code:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:address");
		map.put(Pattern.compile("registrant country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:address");

		map.put(Pattern.compile("admin id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:handle");
		map.put(Pattern.compile("admin name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:name");
		map.put(Pattern.compile("admin organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:organization");
		map.put(Pattern.compile("admin street:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:address");
		map.put(Pattern.compile("admin city:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:address");
		map.put(Pattern.compile("admin state/province:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:address");
		map.put(Pattern.compile("admin postal code:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:address");
		map.put(Pattern.compile("admin country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:address");

		map.put(Pattern.compile("tech id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:handle");
		map.put(Pattern.compile("tech name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:name");
		map.put(Pattern.compile("tech organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:organization");
		map.put(Pattern.compile("tech street:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:address");
		map.put(Pattern.compile("tech city:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:address");
		map.put(Pattern.compile("tech state/province:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:address");
		map.put(Pattern.compile("tech postal code:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:address");
		map.put(Pattern.compile("tech country:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:address");
		blockItems.put(1, map);

		availableItem = Pattern.compile(".*NOT FOUND.*", Pattern.CASE_INSENSITIVE);
	}


}
