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

import pl.nask.hsn2.service.parser.CHWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class CHWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new CHWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/punkt.ch.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);

		Assert.assertFalse(parser.isAvailable(data));

		Assert.assertEquals(res.get("contacts:owner:name"), "Punkt Tronics AG");
		Assert.assertEquals(res.get("contacts:tech:name"), "Register.it S.p.A.");
		Assert.assertEquals(res.get("registrar:name"), "Register.it S.p.A.");
		Assert.assertEquals(res.get("created"), "1996-12-18");
		Assert.assertEquals(res.get("dnssec"), "N");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.register.it"));
		Assert.assertTrue(nss.contains("ns2.register.it"));
	}

	@Test
	public void dataParsingTest2() throws IOException {
		WhoIsParser parser = new CHWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nic.ch.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);

		Assert.assertFalse(parser.isAvailable(data));

		Assert.assertEquals(res.get("contacts:owner:name"), "SWITCH");
		Assert.assertEquals(res.get("contacts:tech:name"), "SWITCH");
		Assert.assertEquals(res.get("registrar:name"), "none");
		Assert.assertEquals(res.get("created"), "before 1996-01-01");
}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new CHWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ch.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
