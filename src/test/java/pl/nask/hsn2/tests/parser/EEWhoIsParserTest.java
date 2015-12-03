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

import pl.nask.hsn2.service.parser.EEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class EEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new EEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/bl.ee.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2011-06-18 15:09:00 +03:00");
		Assert.assertEquals(res.get("changed"), "2014-12-22 16:01:37 +02:00");
		Assert.assertEquals(res.get("expires"), "2016-06-18");
		Assert.assertEquals(res.get("status"), "ok (paid and in zone)");

		Assert.assertEquals(res.get("contacts:owner:name"), "Aurimas Rapalis");
		Assert.assertEquals(res.get("contacts:admin:name"), "Allan Laal");
		Assert.assertEquals(res.get("contacts:tech:name"), "Allan Laal");
		Assert.assertEquals(res.get("registrar:name"), "Radicenter OÜ");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("ns4.hostinger.com"));
		Assert.assertTrue(nss.contains("ns3.hostinger.com"));
		Assert.assertTrue(nss.contains("ns2.hostinger.com"));
		Assert.assertTrue(nss.contains("ns1.hostinger.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new EEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ee.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
