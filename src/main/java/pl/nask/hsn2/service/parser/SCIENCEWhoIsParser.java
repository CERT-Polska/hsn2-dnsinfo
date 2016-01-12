package pl.nask.hsn2.service.parser;

import java.util.regex.Pattern;

public final class SCIENCEWhoIsParser extends AbstractNICWhoIsParser {
	public SCIENCEWhoIsParser() {
		super();

		availableItem = Pattern.compile("No Domain exists", Pattern.CASE_INSENSITIVE);
	}
}
