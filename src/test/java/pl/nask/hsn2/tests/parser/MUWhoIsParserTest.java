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

import pl.nask.hsn2.service.parser.MUWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class MUWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new MUWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nic.mu.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "1999-12-30T13:00:00.000Z");
		Assert.assertEquals(res.get("expires"), "2024-12-30T20:00:00.000Z");
		Assert.assertEquals(res.get("changed"), "2014-12-05T12:51:38.098Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");

		Assert.assertEquals(res.get("contacts:owner:name"), "Internet Direct");
		Assert.assertEquals(res.get("contacts:admin:name"), "Internet Direct");
		Assert.assertEquals(res.get("registrar:name"), "Mauritius Domains");

		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> status = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(status.size(), 9);
		Assert.assertTrue(status.contains("ok"));
		Assert.assertTrue(status.contains("clientUpdateProhibited"));
		Assert.assertTrue(status.contains("clientRenewProhibited"));
		Assert.assertTrue(status.contains("clientTransferProhibited"));
		Assert.assertTrue(status.contains("clientDeleteProhibited"));
		Assert.assertTrue(status.contains("serverUpdateProhibited"));
		Assert.assertTrue(status.contains("serverRenewProhibited"));
		Assert.assertTrue(status.contains("serverTransferProhibited"));
		Assert.assertTrue(status.contains("serverDeleteProhibited"));

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 5);
		Assert.assertTrue(nss.contains("ns1.nic.mu"));
		Assert.assertTrue(nss.contains("ns2.nic.mu"));
		Assert.assertTrue(nss.contains("ns3.nic.mu"));
		Assert.assertTrue(nss.contains("ns4.nic.mu"));
		Assert.assertTrue(nss.contains("ns1.tld.mu"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new MUWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.mu.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
