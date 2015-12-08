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

import pl.nask.hsn2.service.parser.AUWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class AUWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new AUWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/fullo.com.au.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("changed"), "17-Jun-2015 11:02:33 UTC");

		Assert.assertEquals(res.get("registrar:name"), "GoDaddy.com, LLC");
		Assert.assertEquals(res.get("contacts:owner:name"), "M.S HERGOTT & K TANG & D.J WINDER");
		Assert.assertEquals(res.get("contacts:admin:name"), "David Winder");
		Assert.assertEquals(res.get("contacts:admin:handle"), "CR197149990");
		Assert.assertEquals(res.get("contacts:tech:name"), "David Winder");
		Assert.assertEquals(res.get("contacts:tech:handle"), "CR197149991");

		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> status = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(status.size(), 2);
		Assert.assertTrue(status.contains("clientDeleteProhibited"));
		Assert.assertTrue(status.contains("clientUpdateProhibited"));

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns31.domaincontrol.com"));
		Assert.assertTrue(nss.contains("ns32.domaincontrol.com"));

		Assert.assertEquals(res.get("dnssec"), "unsigned");

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new AUWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.au.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
