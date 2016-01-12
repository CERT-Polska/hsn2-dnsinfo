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

import pl.nask.hsn2.service.parser.SCIENCEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class SCIENCEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new SCIENCEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/bootriver.science.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2015-04-10T19:05:29Z");
		Assert.assertEquals(res.get("expires"), "2016-04-09T23:59:59Z");
		Assert.assertEquals(res.get("changed"), "2015-04-10T21:59:50Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "ok");

		Assert.assertEquals(res.get("contacts:owner:handle"), "C266019-SCIENCE");
		Assert.assertEquals(res.get("contacts:owner:name"), "DomainManager");
		Assert.assertEquals(res.get("contacts:owner:organization"), "N/A");

		Assert.assertEquals(res.get("contacts:admin:handle"), "C266019-SCIENCE");
		Assert.assertEquals(res.get("contacts:admin:name"), "DomainManager");
		Assert.assertEquals(res.get("contacts:admin:organization"), "N/A");

		Assert.assertEquals(res.get("contacts:tech:handle"), "C266019-SCIENCE");
		Assert.assertEquals(res.get("contacts:tech:name"), "DomainManager");
		Assert.assertEquals(res.get("contacts:tech:organization"), "N/A");

		Assert.assertEquals(res.get("registrar:id"), "1857");
		Assert.assertEquals(res.get("registrar:name"), "Alpnames Limited");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS1.DOMAINMANAGER.COM"));
		Assert.assertTrue(nss.contains("NS2.DOMAINMANAGER.COM"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new SCIENCEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.science.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
