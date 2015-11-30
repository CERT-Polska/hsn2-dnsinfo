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

import pl.nask.hsn2.service.parser.NZWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class NZWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new NZWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/freewebhost.co.nz.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2009-08-25T09:38:34+12:00");
		Assert.assertEquals(res.get("expires"), "2016-08-25T09:38:34+12:00");
		Assert.assertEquals(res.get("changed"), "2015-08-28T10:07:06+12:00");
		Assert.assertEquals(res.get("dnssec"), "no");
		Assert.assertEquals(res.get("status"), "200 Active");

		Assert.assertEquals(res.get("contacts:owner:name"), "One IT Limited");
		Assert.assertEquals(res.get("contacts:owner:email"), "brett@oneit.co.nz");
		Assert.assertEquals(res.get("contacts:owner:country"), "NZ (NEW ZEALAND)");

		Assert.assertEquals(res.get("contacts:admin:name"), "Brett Smith");
		Assert.assertEquals(res.get("contacts:admin:email"), "info@oneit.co.nz");

		Assert.assertEquals(res.get("contacts:tech:name"), "Brett Smith");
		Assert.assertEquals(res.get("contacts:tech:email"), "info@oneit.co.nz");

		Assert.assertEquals(res.get("registrar:name"), "Crazy Domains FZ-LLC");
		Assert.assertEquals(res.get("registrar:email"), "registry@crazydomains.com");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("ns1.byet.org"));
		Assert.assertTrue(nss.contains("ns2.byet.org"));
		Assert.assertTrue(nss.contains("ns3.byet.org"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new NZWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.nz.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
