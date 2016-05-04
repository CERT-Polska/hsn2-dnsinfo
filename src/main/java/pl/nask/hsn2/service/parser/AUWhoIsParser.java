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

public final class AUWhoIsParser extends AbstractRegExpWhoisParser {
	public AUWhoIsParser() {
		blocks.put(1, Pattern.compile("domain name:(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("last modified:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "changed");
		map.put(Pattern.compile("status:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("registrar name:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("registrant:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("registrant contact id:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:handle");
		map.put(Pattern.compile("registrant contact name:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:admin:name");
		map.put(Pattern.compile("tech contact id:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:handle");
		map.put(Pattern.compile("tech contact name:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		map.put(Pattern.compile("name server:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("name server ip:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "ips");
		map.put(Pattern.compile("dnssec:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(1, map);

		availableItem = Pattern.compile("no data found", Pattern.CASE_INSENSITIVE);
	}
}
