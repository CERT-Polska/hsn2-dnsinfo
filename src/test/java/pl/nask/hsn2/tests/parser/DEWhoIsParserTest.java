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

import pl.nask.hsn2.service.parser.DEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class DEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new DEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/denic.de.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertFalse(parser.isAvailable(data));

		Assert.assertEquals(res.get("status"), "connect");
		Assert.assertEquals(res.get("dnssec"), "257 3 8 AwEAAb/xrM2MD+xm84YNYby6TxkMaC6PtzF2bB9WBB7ux7iqzhViob4GKvQ6L7CkXjyAxfKbTzrdvXoAPpsAPW4pkThReDAVp3QxvUKrkBM8/uWRF3wpaUoPsAHm1dbcL9aiW3lqlLMZjDEwDfU6lxLcPg9d14fq4dc44FvPx6aYcymkgJoYvR6P1wECpxqlEAR2K1cvMtqCqvVESBQV/EUtWiALNuwR2PbhwtBWJd+e8BdFI7OLkit4uYYux6Yu35uyGQ==");
		Assert.assertEquals(res.get("changed"), "2015-02-11T14:56:06+01:00");

		Assert.assertEquals(res.get("contacts:tech:type"), "PERSON");
		Assert.assertEquals(res.get("contacts:tech:name"), "Business Services");
		Assert.assertEquals(res.get("contacts:tech:organization"), "DENIC eG");
		Assert.assertEquals(res.get("contacts:tech:country"), "DE");
		Assert.assertEquals(res.get("contacts:tech:changed"), "2010-12-15T12:00:30+01:00");

		Assert.assertEquals(res.get("contacts:zone:type"), "PERSON");
		Assert.assertEquals(res.get("contacts:zone:name"), "Business Services");
		Assert.assertEquals(res.get("contacts:zone:organization"), "DENIC eG");
		Assert.assertEquals(res.get("contacts:zone:country"), "DE");
		Assert.assertEquals(res.get("contacts:zone:changed"), "2010-12-15T12:00:30+01:00");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("ns1.denic.de 2a02:568:121:6:2:0:0:2 81.91.170.1"));
		Assert.assertTrue(nss.contains("ns2.denic.de 78.104.145.26"));
		Assert.assertTrue(nss.contains("ns3.denic.de 81.91.173.19"));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new DEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.de.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
