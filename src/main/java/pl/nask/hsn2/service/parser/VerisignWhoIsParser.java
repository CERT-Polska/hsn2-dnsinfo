package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class VerisignWhoIsParser extends RegExpWhoisParser {

	

	public VerisignWhoIsParser() {
		super();
//		protected $blocks = array(1 => '/domain name:(?>[\x20\t]*)(.*?)(?=>>>)/is');
		blocks.put(1,
				Pattern.compile(
						".*domain name:(?>[\\x20\\t]*)(.*?)(?=>>>).*",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		Map<Pattern, String> map;
//      1 => array('/whois server:(?>[\x20\t]*)(.+)$/im' => 'whoisserver', 
//      '/registrar:(?>[\x20\t]*)(.+)$/im' => 'registrar:name', 
//      '/registrar iana id:(?>[\x20\t]*)(.+)$/im' => 'registrar:id', 
//      '/referral url:(?>[\x20\t]*)(.+)$/im' => 'registrar:url', 
//      '/creation date:(?>[\x20\t]*)(.+)$/im' => 'created', 
//      '/expiration date:(?>[\x20\t]*)(.+)$/im' => 'expires', 
//      '/updated date:(?>[\x20\t]*)(.+)$/im' => 'changed', 
//      '/name server:(?>[\x20\t]*)(.+)$/im' => 'nameserver', 
//      '/dnssec:(?>[\x20\t]*)(.+)$/im' => 'dnssec', 
//      '/status:(?>[\x20\t]*)(.+)$/im' => 'status'));
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("whois server:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "whoisserver");
		map.put(Pattern.compile("registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:name");
		map.put(Pattern.compile("registrar iana id:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:id");
		map.put(Pattern.compile("referral url:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:url");
		map.put(Pattern.compile("creation date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"created");
		map.put(Pattern.compile("expiration date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"expires");
		map.put(Pattern.compile("updated date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"changed");
		map.put(Pattern.compile("name server:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"nameservers");
		map.put(Pattern.compile("dnssec:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"dnssec");
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"status");
		blockItems.put(1, map);
		
//		 protected $available = '/No match for/i';
		availableItem = Pattern.compile(".*No match for.*", Pattern.CASE_INSENSITIVE);
	}
}
