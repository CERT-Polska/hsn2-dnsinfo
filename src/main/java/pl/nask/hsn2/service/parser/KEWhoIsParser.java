package pl.nask.hsn2.service.parser;

import java.util.regex.Pattern;

public final class KEWhoIsParser extends AbstractNICWhoIsParser {
	public KEWhoIsParser() {
		super();

		availableItem = Pattern.compile("Domain Status:(?>[\\s]*)No Object Found", Pattern.CASE_INSENSITIVE);
	}
}
