/*
 * Copyright (c) NASK
 * 
 * This file is part of HoneySpider Network 2.1.
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

public final class MEWhoIsParser extends AbstractRegExpWhoisParser {
	public MEWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++,
				Pattern.compile(
						"domain id:(?>[\\x20\\t]*)(.*?)$",
						Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;
		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("Sponsoring Registrar:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("Domain Status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"status");
		map.put(Pattern.compile("Domain Create Date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		map.put(Pattern.compile("Domain Last Updated Date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("Domain Expiration Date:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "expires");

		map.put(Pattern.compile("Registrant Name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:owner:name");
		map.put(Pattern.compile("Registrant ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:handle");
		map.put(Pattern.compile("Registrant Organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:organization");
		map.put(Pattern.compile("Registrant Address:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("Registrant Postal Code:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("Registrant City:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("Registrant State/Province:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");
		map.put(Pattern.compile("Registrant Country/Economy:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:address");

		map.put(Pattern.compile("Admin Name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:name");
		map.put(Pattern.compile("Admin ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("Admin Organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:admin:organization");
		map.put(Pattern.compile("Admin Address:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:address");
		map.put(Pattern.compile("Admin Postal Code:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:address");
		map.put(Pattern.compile("Admin City:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:address");
		map.put(Pattern.compile("Admin State/Province:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:address");
		map.put(Pattern.compile("Admin Country/Economy:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:address");

		map.put(Pattern.compile("Tech Name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:name");
		map.put(Pattern.compile("Tech ID:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("Tech Organization:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),"contacts:tech:organization");
		map.put(Pattern.compile("Tech Address:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");
		map.put(Pattern.compile("Tech Postal Code:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");
		map.put(Pattern.compile("Tech City:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");
		map.put(Pattern.compile("Tech State/Province:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");
		map.put(Pattern.compile("Tech Country/Economy:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:address");

		map.put(Pattern.compile("Nameservers:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("DNSSEC:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE  | Pattern.MULTILINE), "dnssec");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("NOT FOUND", Pattern.CASE_INSENSITIVE);
	}
}
