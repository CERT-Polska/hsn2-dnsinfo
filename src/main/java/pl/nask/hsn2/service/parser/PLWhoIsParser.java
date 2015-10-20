package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class PLWhoIsParser extends AbstractRegExpWhoisParser {

	public PLWhoIsParser() {
		super();
//	    protected $blocks = array(
//      1 => '/domain name:(?>[\x20\t]*)(.*?)(?=technical contact:|registrar:)/is', 
//      2 => '/technical contact:(?>[\x20\t]*)(.*?)(?=registrar:)/is', 
//      3 => '/registrar:(?>[\x20\t]*)(.*?)(?=WHOIS displays data)/is');
		int blockNumber = 1;
		blocks.put(blockNumber++,
				Pattern.compile(
						"domain name:(?>[\\x20\\t]*)(.*?)(?=technical contact:|registrar:)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++,
				Pattern.compile(
						"technical contact:(?>[\\x20\\t]*)(.*?)(?=registrar:)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++,
				Pattern.compile(
						"registrar:(?>[\\x20\\t]*)(.*?)(?=WHOIS displays data)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		
		blockNumber = 1;

		Map<Pattern, String> map;
//      1 => array('/^(nameservers:)?(?>[\x20\t]+)(.+)\./im' => 'nameserver', 
//              '/^(nameservers:)?(?>[\x20\t]+)(.+)\. \[.+\]/im' => 'nameserver', 
//              '/^created:(?>[\x20\t]*)(.+)$/im' => 'created', 
//              '/last modified:(?>[\x20\t]*)(.+)$/im' => 'changed', 
//              '/renewal date:(?>[\x20\t]*)(.+)$/im' => 'expires', 
//              '/dnssec:(?>[\x20\t]*)(.+)$/im' => 'dnssec'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("^(?>nameservers:)?(?>[\\x20\\t]+)(.+)\\.[\\x20\\t]*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"nameservers");
		map.put(Pattern.compile("^(?>nameservers:)?(?>[\\x20\\t]+)(.+)\\. \\[.+\\]$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("^created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("last modified:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("renewal date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		map.put(Pattern.compile("dnssec:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(blockNumber++, map);

//      2 => array('/company:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:name', 
//              '/^(?>[\x20\t]+)(.+)$/im' => 'contacts:tech:organization', 
//              '/street:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:address', 
//              '/city:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:city', 
//              '/location:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:country', 
//              '/handle:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:handle', 
//              '/phone:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:phone', 
//              '/fax:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:fax', 
//              '/last modified:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:changed'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("company:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:name");
		map.put(Pattern.compile("^(?>[\\x20\\t]+)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:organization");
		map.put(Pattern.compile("street:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");
		map.put(Pattern.compile("city:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:tech:city");
		map.put(Pattern.compile("location:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:country");
		map.put(Pattern.compile("handle:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("phone:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:phone");
		map.put(Pattern.compile("fax:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:fax");
		map.put(Pattern.compile("last modified:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:changed");
		blockItems.put(blockNumber++, map);

      
//      3 => array('/registrar:\n(.*)$/im' => 'registrar:name', 
//              '/(?=fax:).+\n(.+)\n\n$/is' => 'registrar:email'));
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("registrar:\n(.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:name");
		map.put(Pattern.compile("(?=fax:).+\n(.+)\n\n$", Pattern.CASE_INSENSITIVE  | Pattern.DOTALL), "registrar:email");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("No information available about domain", Pattern.CASE_INSENSITIVE);
	}
}
