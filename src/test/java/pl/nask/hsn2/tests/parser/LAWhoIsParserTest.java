package pl.nask.hsn2.tests.parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import pl.nask.hsn2.service.parser.LAWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class LAWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new LAWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/royaldumps.la.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2014-01-23T18:08:21.0Z");
		Assert.assertEquals(res.get("expires"), "2016-01-23T23:59:59.0Z");
		Assert.assertEquals(res.get("changed"), "2015-01-19T03:12:06.0Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "clientTransferProhibited");

		Assert.assertEquals(res.get("contacts:owner:handle"), "C4984769-CNIC");
		Assert.assertEquals(res.get("contacts:owner:name"), "Rebecca J. Caban");
		Assert.assertEquals(res.get("contacts:owner:organization"), "N/A");

		Assert.assertEquals(res.get("contacts:admin:handle"), "C4984772-CNIC");
		Assert.assertEquals(res.get("contacts:admin:name"), "Rebecca J. Caban");
		Assert.assertEquals(res.get("contacts:admin:organization"), "N/A");

		Assert.assertEquals(res.get("contacts:tech:handle"), "C4984772-CNIC");
		Assert.assertEquals(res.get("contacts:tech:name"), "Rebecca J. Caban");
		Assert.assertEquals(res.get("contacts:tech:organization"), "N/A");

		Assert.assertEquals(res.get("registrar:id"), "697");
		Assert.assertEquals(res.get("registrar:name"), "ERANET INTERNATIONAL LIMITED");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("NS1.RITORTGOLF.AT"));
		Assert.assertTrue(nss.contains("NS2.RITORTGOLF.AT"));
		Assert.assertTrue(nss.contains("NS1.URDUPEOPLE.RU"));
		Assert.assertTrue(nss.contains("NS2.URDUPEOPLE.RU"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new LAWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.la.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
