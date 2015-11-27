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

import pl.nask.hsn2.service.parser.INWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class INWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new INWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/registry.in.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);


		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "16-Dec-2004 16:27:39 UTC");
		Assert.assertEquals(res.get("expires"), "30-Dec-2015 16:27:39 UTC");
		Assert.assertEquals(res.get("changed"), "30-Dec-2014 22:30:20 UTC");
		Assert.assertEquals(res.get("dnssec"), "Signed");
		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> status = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(status.size(), 4);
		Assert.assertTrue(status.contains("CLIENT DELETE PROHIBITED"));
		Assert.assertTrue(status.contains("CLIENT RENEW PROHIBITED"));
		Assert.assertTrue(status.contains("CLIENT TRANSFER PROHIBITED"));
		Assert.assertTrue(status.contains("DELETE PROHIBITED"));

		Assert.assertEquals(res.get("contacts:owner:handle"), "FR-1187763823079");
		Assert.assertEquals(res.get("contacts:owner:name"), "National Internet Exchange of India");

		Assert.assertEquals(res.get("contacts:admin:handle"), "FR-1187763823079");
		Assert.assertEquals(res.get("contacts:admin:name"), "National Internet Exchange of India");

		Assert.assertEquals(res.get("contacts:tech:handle"), "FR-1187763823079");
		Assert.assertEquals(res.get("contacts:tech:name"), "National Internet Exchange of India");

		Assert.assertEquals(res.get("registrar:name"), "National Internet Exchange of India (R36-AFIN)");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 5);
		Assert.assertTrue(nss.contains("NS1.AMS1.AFILIAS-NST.INFO"));
		Assert.assertTrue(nss.contains("NS1.MIA1.AFILIAS-NST.INFO"));
		Assert.assertTrue(nss.contains("NS1.SEA1.AFILIAS-NST.INFO"));
		Assert.assertTrue(nss.contains("NS1.YYZ1.AFILIAS-NST.INFO"));
		Assert.assertTrue(nss.contains("NS1.HKG1.AFILIAS-NST.INFO"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new INWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.in.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
