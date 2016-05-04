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

public final class LTWhoIsParser extends AbstractRegExpWhoisParser {
	public LTWhoIsParser() {
		super();
		blocks.put(1, Pattern.compile("domain:(?>[\\x20\\t]*)(.*?)$", Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

		Map<Pattern, String> map = new HashMap<>();
		map.put(Pattern.compile("status:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "status");
		map.put(Pattern.compile("registered:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "created");

		map.put(Pattern.compile("registrar:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:name");
		map.put(Pattern.compile("registrar website:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:url");
		map.put(Pattern.compile("registrar email:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "registrar:email");

		map.put(Pattern.compile("contact name:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:name");
		map.put(Pattern.compile("contact organization:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:organization");
		map.put(Pattern.compile("contact email:(?>[\\s]*)(.*?)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "contacts:owner:email");

		map.put(Pattern.compile("nameserver:(?>[\\s]*)([^\\s]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE), "nameservers");
		blockItems.put(1, map);

		availableItem = Pattern.compile("status:(?>[\\s]*)available$", Pattern.CASE_INSENSITIVE);
	}
}
