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

import pl.nask.hsn2.service.parser.ROWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class ROWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new ROWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/computer.ro.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2006-08-02");
		Assert.assertEquals(res.get("registrar:name"), "HELL ADVERTISING CORPORATE SRL");
		Assert.assertEquals(res.get("status"), "OK");
		
		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("ns1.domain.ro"));
		Assert.assertTrue(nss.contains("ns2.domain.ro"));
		Assert.assertTrue(nss.contains("ns3.domain.ro"));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new ROWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ro.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
