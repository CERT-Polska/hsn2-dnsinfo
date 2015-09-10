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

import pl.nask.hsn2.service.parser.RUWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class RUWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new RUWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/soccer.ru.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("contacts:owner:name"), "Private Person");
		Assert.assertEquals(res.get("expires"), "2015.12.16");
		Assert.assertEquals(res.get("created"), "2002.12.16");
		Assert.assertEquals(res.get("registrar:id"), "RU-CENTER-RU");
		Assert.assertEquals(res.get("status"), "REGISTERED, DELEGATED, UNVERIFIED");
		
		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.hc.ru."));
		Assert.assertTrue(nss.contains("ns2.hc.ru."));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new RUWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ru.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
