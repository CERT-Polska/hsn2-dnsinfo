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

import pl.nask.hsn2.service.parser.BEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class BEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new BEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/dnsbelgium.be.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "Wed Jun 13 2012");
		Assert.assertEquals(res.get("status"), "NOT AVAILABLE");

		Assert.assertEquals(res.get("registrar:name"), "DNS BE vzw/asbl");
		Assert.assertEquals(res.get("registrar:url"), "http://www.dns.be");

		Assert.assertEquals(res.get("contacts:tech:name"), "DNS BE Technical Staff");
		Assert.assertEquals(res.get("contacts:tech:organization"), "DNS Belgium vzw");
		Assert.assertEquals(res.get("contacts:tech:email"), "tech@dns.be");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.dns.be"));
		Assert.assertTrue(nss.contains("ns2.dns.be"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new BEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.be.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
