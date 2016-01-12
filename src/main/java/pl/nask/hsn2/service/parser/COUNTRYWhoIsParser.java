package pl.nask.hsn2.service.parser;

import java.util.regex.Pattern;

public final class COUNTRYWhoIsParser extends AbstractNICWhoIsParser {
	public COUNTRYWhoIsParser() {
		super();

		availableItem = Pattern.compile("Status: Not Registered", Pattern.CASE_INSENSITIVE);
	}
}
