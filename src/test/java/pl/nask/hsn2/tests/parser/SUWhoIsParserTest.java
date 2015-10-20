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

import pl.nask.hsn2.service.parser.SUWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class SUWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new SUWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/natyazhnye-potolki.su.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("contacts:owner:name"), "Private Person");
		Assert.assertEquals(res.get("expires"), "2016.10.10");
		Assert.assertEquals(res.get("created"), "2012.10.10");
		Assert.assertEquals(res.get("registrar:id"), "R01-REG-FID");
		Assert.assertEquals(res.get("status"), "REGISTERED, DELEGATED");
		
		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("ns1.timeweb.ru."));
		Assert.assertTrue(nss.contains("ns2.timeweb.ru."));
		Assert.assertTrue(nss.contains("ns3.timeweb.org."));
		Assert.assertTrue(nss.contains("ns4.timeweb.org."));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new SUWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.su.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
