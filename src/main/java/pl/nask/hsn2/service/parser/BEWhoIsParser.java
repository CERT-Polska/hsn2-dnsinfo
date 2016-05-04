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

public final class BEWhoIsParser extends AbstractRegExpWhoisParser {
	public BEWhoIsParser() {
		super();

		int blockNumber = 1;
		blocks.put(blockNumber++, Pattern.compile("domain:(?>[\\x20\\t]*)(.*?)(?=registrant:)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("registrar technical contacts:(?>[\\x20\\t]*)(.*?)(?=registrar:)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("registrar:(?>[\\x20\\t]*)(.*?)(?=nameservers:)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("nameservers:(?>[\\x20\\t]*)(.*?)(?=keys:)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		blocks.put(blockNumber++, Pattern.compile("keys:(?>[\\x20\\t]*)(.*?)(?=please visit)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		blockNumber = 1;
		Map<Pattern, String> map;

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("status:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("registered:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:name");
		map.put(Pattern.compile("organisation:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:organization");
		map.put(Pattern.compile("phone:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:phone");
		map.put(Pattern.compile("email:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:tech:email");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("name:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("website:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:url");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("\\n(?>[\\x20\\t]+)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("\\n(?>[\\x20\\t]+)(.+) \\(.+\\)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		map.put(Pattern.compile("\\n(?>[\\x20\\t]+).+ \\((.+)\\)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "ips");
		blockItems.put(blockNumber++, map);

		map = new HashMap<Pattern, String>();
		map.put(Pattern.compile("keyTag:(?>[\\x20\\t]*)(.+)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "dnssec");
		blockItems.put(blockNumber++, map);

		availableItem = Pattern.compile("Status:(?>[\\x20\\t]*)AVAILABLE", Pattern.CASE_INSENSITIVE);
	}
}
