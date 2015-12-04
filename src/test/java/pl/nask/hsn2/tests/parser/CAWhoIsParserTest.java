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

import pl.nask.hsn2.service.parser.CAWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class CAWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new CAWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/cira.ca.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "1998/02/05");
		Assert.assertEquals(res.get("expires"), "2050/02/05");
		Assert.assertEquals(res.get("changed"), "2014/11/25");
		Assert.assertEquals(res.get("dnssec"), "Unsigned");
		Assert.assertEquals(res.get("status"), "registered");

		Assert.assertEquals(res.get("contacts:owner:name"), "Canadian Internet Registration Authority (NFP) / Autorité Canadienne pour les enregistrements Internet (OSBL)");
		Assert.assertEquals(res.get("contacts:admin:name"), "Tanya O'Callaghan");
		Assert.assertEquals(res.get("contacts:tech:name"), "Address Reply To");
		Assert.assertEquals(res.get("registrar:name"), "Please contact CIRA at 1-877-860-1411 for more information");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("ns01.cira.ca"));
		Assert.assertTrue(nss.contains("ns02.cira.ca"));
		Assert.assertTrue(nss.contains("ns1.d-zone.ca"));
		Assert.assertTrue(nss.contains("ns2.d-zone.ca"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void lessInfoDataParsingTest() throws IOException {
		WhoIsParser parser = new CAWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/rashidi.ca.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2010/12/20");
		Assert.assertEquals(res.get("expires"), "2015/12/20");
		Assert.assertEquals(res.get("changed"), "2015/02/03");
		Assert.assertEquals(res.get("dnssec"), "Unsigned");
		Assert.assertEquals(res.get("status"), "registered");

		Assert.assertEquals(res.get("registrar:name"), "Go Daddy Domains Canada, Inc");
		Assert.assertEquals(res.get("registrar:id"), "2316042");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns01.domaincontrol.com"));
		Assert.assertTrue(nss.contains("ns02.domaincontrol.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new CAWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ca.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
