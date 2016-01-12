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

import pl.nask.hsn2.service.parser.COUNTRYWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class COUNTRYWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new COUNTRYWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/camppassenger.country.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2015-01-07T17:05:00Z");
		Assert.assertEquals(res.get("expires"), "2017-01-07T17:05:00Z");
		Assert.assertEquals(res.get("changed"), "2016-01-08T00:08:38Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "ok");

		Assert.assertEquals(res.get("contacts:owner:handle"), "283652-MMd1");
		Assert.assertEquals(res.get("contacts:owner:name"), "PrivacyDotLink Customer 302335");

		Assert.assertEquals(res.get("contacts:admin:handle"), "283652-MMd1");
		Assert.assertEquals(res.get("contacts:admin:name"), "PrivacyDotLink Customer 302335");

		Assert.assertEquals(res.get("contacts:tech:handle"), "283652-MMd1");
		Assert.assertEquals(res.get("contacts:tech:name"), "PrivacyDotLink Customer 302335");

		Assert.assertEquals(res.get("registrar:id"), "1659");
		Assert.assertEquals(res.get("registrar:name"), "Uniregistry");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns2.domainmanager.com"));
		Assert.assertTrue(nss.contains("ns1.domainmanager.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new COUNTRYWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.country.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
