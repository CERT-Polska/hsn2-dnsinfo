package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class ITWhoIsParser extends RegExpWhoisParser {

	public ITWhoIsParser() {
		super();

		//		 protected $blocks = array(1 => '/domain:(?>[\x20\t]*)(.*?)(?=registrant)/is', 
//        2 => '/registrant(?>[\x20\t]*)(.*?)(?=admin contact)/is', 
//        3 => '/admin contact(?>[\x20\t]*)(.*?)(?=technical contacts)/is', 
//        4 => '/technical contacts(?>[\x20\t]*)(.*?)(?=registrar)/is', 
//        5 => '/registrar(?>[\x20\t]*)(.*?)(?=nameservers)/is', 
//        6 => '/nameservers(?>[\x20\t]*)(.*?)$/is');
		blocks.put(1,
				Pattern.compile(
						"domain:(?>[\\x20\\t]*)(.*?)(?=registrant)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(2,
				Pattern.compile(
						"registrant(?>[\\x20\\t]*)(.*?)(?=admin contact)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(3,
				Pattern.compile(
						"admin contact(?>[\\x20\\t]*)(.*?)(?=technical contacts)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(4,
				Pattern.compile(
						"technical contacts(?>[\\x20\\t]*)(.*?)(?=registrar)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(5,
				Pattern.compile(
						"registrar(?>[\\x20\\t]*)(.*?)(?=nameservers)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(6,
				Pattern.compile(
						"nameservers(?>[\\x20\\t]*)(.*?)$",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		int blockNumber = 1;
		Map<Pattern, String> map;
//      1 => array('/status:(?>[\x20\t]*)(.+)$/im' => 'status', 
//              '/created:(?>[\x20\t]*)(.+)$/im' => 'created', 
//              '/last update:(?>[\x20\t]*)(.+)$/im' => 'changed', 
//              '/expire date:(?>[\x20\t]*)(.+)$/im' => 'expires'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"status");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("last update:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("expire date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");
		blockItems.put(blockNumber++, map);

//      2 => array('/name:(?>[\x20\t]*)(.+)$/im' => 'contacts:owner:name', 
//              '/organization:(?>[\x20\t]*)(.+)$/im' => 'contacts:owner:organization', 
//              '/contactid:(?>[\x20\t]*)(.+)$/im' => 'contacts:owner:handle', 
//              '/address:(?>[\x20\t]*)(.+)(?=created)/is' => 'contacts:owner:address', 
//              '/created:(?>[\x20\t]*)(.+)$/im' => 'contacts:owner:created', 
//              '/last update:(?>[\x20\t]*)(.+)$/im' => 'contacts:owner:changed'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:name");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:organization");
		map.put(Pattern.compile("contactid:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("address:(?>[\\x20\\t]*)(.+)(?=created)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:owner:address");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:created");
		map.put(Pattern.compile("last update:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:changed");
		blockItems.put(blockNumber++, map);

//      3 => array('/name:(?>[\x20\t]*)(.+)$/im' => 'contacts:admin:name', 
//              '/organization:(?>[\x20\t]*)(.+)$/im' => 'contacts:admin:organization', 
//              '/contactid:(?>[\x20\t]*)(.+)$/im' => 'contacts:admin:handle', 
//              '/address:(?>[\x20\t]*)(.+)(?=created)/is' => 'contacts:admin:address', 
//              '/created:(?>[\x20\t]*)(.+)$/im' => 'contacts:admin:created', 
//              '/last update:(?>[\x20\t]*)(.+)$/im' => 'contacts:admin:changed'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:name");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE), "contacts:admin:organization");
		map.put(Pattern.compile("contactid:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("address:(?>[\\x20\\t]*)(.+)(?=created)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:admin:address");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:created");
		map.put(Pattern.compile("last update:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:changed");
		blockItems.put(blockNumber++, map);

//      4 => array('/name:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:name', 
//              '/organization:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:organization', 
//              '/contactid:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:handle', 
//              '/address:(?>[\x20\t]*)(.+)(?=created)/is' => 'contacts:tech:address', 
//              '/created:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:created', 
//              '/last update:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:changed'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:name");
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:organization");
		map.put(Pattern.compile("contactid:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("address:(?>[\\x20\\t]*)(.+)(?=created)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "contacts:tech:address");
		map.put(Pattern.compile("created:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:created");
		map.put(Pattern.compile("last update:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:changed");
		blockItems.put(blockNumber++, map);

//      5 => array('/organization:(?>[\x20\t]*)(.+)$/im' => 'registrar:name', 
//              '/name:(?>[\x20\t]*)(.+)$/im' => 'registrar:id'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE),"registrar:name");
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE), "registrar:id");
		blockItems.put(blockNumber++, map);

//      6 => array('/\n(?>[\x20\t]+)(.+)$/im' => 'nameserver'));
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("\n(?>[\\x20\\t]+)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"nameservers");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile(".*status:(?>[\\x20\\t]*)available.*", Pattern.CASE_INSENSITIVE);
	}
}
