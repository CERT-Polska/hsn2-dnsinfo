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

import pl.nask.hsn2.service.parser.UKWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class UKWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new UKWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/romseychamber.co.uk.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "11-Oct-2007");
		Assert.assertEquals(res.get("changed"), "12-Nov-2015");
		Assert.assertEquals(res.get("expires"), "11-Oct-2016");

		Assert.assertEquals(res.get("registrar:name"), "GoDaddy.com, LLP.");
		Assert.assertEquals(res.get("registrar:id"), "GODADDY");
		Assert.assertEquals(res.get("registrar:url"), "http://uk.godaddy.com");
		Assert.assertEquals(res.get("contacts:owner:name"), "Romsey Business Consortium");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 5);
		Assert.assertTrue(nss.contains("freedns1.registrar-servers.com"));
		Assert.assertTrue(nss.contains("freedns2.registrar-servers.com"));
		Assert.assertTrue(nss.contains("freedns3.registrar-servers.com"));
		Assert.assertTrue(nss.contains("freedns4.registrar-servers.com"));
		Assert.assertTrue(nss.contains("freedns5.registrar-servers.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new UKWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.uk.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
