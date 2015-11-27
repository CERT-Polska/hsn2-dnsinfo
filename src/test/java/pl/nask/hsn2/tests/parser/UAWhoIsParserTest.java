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

import pl.nask.hsn2.service.parser.UAWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class UAWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new UAWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/whois.ua.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);

		Assert.assertEquals(res.get("created"), "2007-06-26 14:01:30+03");
		Assert.assertEquals(res.get("expires"), "2020-06-26 00:00:00+03");
		Assert.assertEquals(res.get("changed"), "2014-03-31 17:30:16+03");
		Assert.assertEquals(res.get("status"), "ok");
		Assert.assertEquals(res.get("registrar:id"), "ua.hostmaster");
		Assert.assertEquals(res.get("registrar:name"), "Hostmaster Ltd");
		Assert.assertEquals(res.get("contacts:owner:name"), "Hostmaster LTD");
		Assert.assertEquals(res.get("contacts:owner:organization"), "Hostmaster LTD");
		Assert.assertEquals(res.get("contacts:admin:name"), "Hostmaster LTD");

		Assert.assertNotNull(res.get("contacts:tech:name"));
		Assert.assertTrue(res.get("contacts:tech:name") instanceof String);
		List<String> tech = Arrays.asList(res.get("contacts:tech:name").split(";"));
		Assert.assertEquals(tech.size(), 2);
		Assert.assertTrue(tech.contains("Hostmaster LTD"));
		Assert.assertTrue(tech.contains("Tkachenko Svitlana"));

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("delta.hostmaster.ua"));
		Assert.assertTrue(nss.contains("ho1.ns.hostmaster.ua"));
		Assert.assertTrue(nss.contains("canada.hostmaster.ua"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new UAWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ua.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
