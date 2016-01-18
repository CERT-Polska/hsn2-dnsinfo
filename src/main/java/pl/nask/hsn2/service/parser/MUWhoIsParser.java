package pl.nask.hsn2.service.parser;

import java.util.regex.Pattern;

public final class MUWhoIsParser extends AbstractNICWhoIsParser {
	public MUWhoIsParser() {
		super();

		availableItem = Pattern.compile("Domain Status: No Object Found", Pattern.CASE_INSENSITIVE);
	}
}
