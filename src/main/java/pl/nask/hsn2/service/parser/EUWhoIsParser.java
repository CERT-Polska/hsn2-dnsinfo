/*
 * Copyright (c) NASK
 * 
 * This file is part of HoneySpider Network 2.0.
 * 
 * This is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.nask.hsn2.service.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class EUWhoIsParser extends AbstractRegExpWhoisParser {

	
	public EUWhoIsParser() {
		super();

//	    protected $blocks = array(1 => '/technical:\n(.*?)(?=registrar)/is', 
//      2 => '/registrar:\n(.*?)(?=name servers)/is',
//		  3 => '/name servers:\n(.*?)(?=keys:)/is', 
//      4 => '/keys:\n(.*?)(?=Please visit)/is');
		int blockNumber = 1;
		blocks.put(blockNumber++,
				Pattern.compile(
						"technical:\n(.*?)(?=registrar)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++,
				Pattern.compile(
						"registrar:\n(.*?)(?=name servers)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++,
				Pattern.compile(
						"name servers:\n(.*?)(?=keys:|Please visit)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++,
				Pattern.compile(
						"keys:\n(.*?)(?=Please visit)",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;
//      1 => array('/name:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:name', 
//              '/organisation:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:organization', 
//              '/language:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:language', 
//              '/phone:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:phone', 
//              '/fax:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:fax', 
//              '/email:(?>[\x20\t]*)(.+)$/im' => 'contacts:tech:email'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:name");
		map.put(Pattern.compile("organisation:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:organization");
		map.put(Pattern.compile("language:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:language");
		map.put(Pattern.compile("phone:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:phone");
		map.put(Pattern.compile("fax:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:fax");
		map.put(Pattern.compile("email:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:email");
		blockItems.put(blockNumber++, map);

//      2 => array('/name:(?>[\x20\t]*)(.+)$/im' => 'registrar:name', 
//              '/website:(?>[\x20\t]*)(.+)$/im' => 'registrar:url'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"registrar:name");
		map.put(Pattern.compile("website:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:url");
		blockItems.put(blockNumber++, map);

//      3 => array('/\n(?>[\x20\t]+)(.+)$/im' => 'nameserver', 
//              '/\n(?>[\x20\t]+)(.+) \(.+\)$/im' => 'nameserver', 
//              '/\n(?>[\x20\t]+).+ \((.+)\)$/im' => 'ips'), 
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("(?>[\\x20\\t]+)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"nameservers");
		map.put(Pattern.compile("(?>[\\x20\\t]+)(.+) \\(.+\\)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("(?>[\\x20\\t]+).+ \\((.+)\\)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "ips");
		blockItems.put(blockNumber++, map);

//      4 => array('/keyTag:(.+)$/im' => 'dnssec'));
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("keyTag:(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"dnssec");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile(".*Status:(?>[\\x20\\t]*)AVAILABLE.*", Pattern.CASE_INSENSITIVE);
	}
}
