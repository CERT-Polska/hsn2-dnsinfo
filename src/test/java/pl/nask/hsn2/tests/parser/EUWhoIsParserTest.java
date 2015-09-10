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

import pl.nask.hsn2.service.parser.EUWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class EUWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new EUWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/dataconsultsrl.eu.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("contacts:tech:name"), "Technical Support");
		Assert.assertEquals(res.get("contacts:tech:organization"), "Register.it Spa");
		Assert.assertEquals(res.get("contacts:tech:phone"), "+39.0353230400");
		Assert.assertEquals(res.get("contacts:tech:fax"), "+39.0353230312");
		Assert.assertEquals(res.get("contacts:tech:email"), "support@register.it");
		Assert.assertEquals(res.get("contacts:tech:language"), "it");
		Assert.assertEquals(res.get("registrar:name"), "Register.it S.p.A.");
		Assert.assertEquals(res.get("registrar:url"), "www.register.it");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.register.it"));
		Assert.assertTrue(nss.contains("ns2.register.it"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new EUWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.eu.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
