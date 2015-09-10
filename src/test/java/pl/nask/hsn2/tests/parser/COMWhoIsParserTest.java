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

import pl.nask.hsn2.service.parser.COMWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class COMWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new COMWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/adagioartagency.com.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("whoisserver"), "whois.register.it");
		Assert.assertEquals(res.get("status"), "ok http://www.icann.org/epp#OK");
		Assert.assertEquals(res.get("created"), "04-mar-2014");
		Assert.assertEquals(res.get("changed"), "05-mar-2015");
		Assert.assertEquals(res.get("expires"), "04-mar-2016");
		Assert.assertEquals(res.get("registrar:id"), "168");
		Assert.assertEquals(res.get("registrar:name"), "REGISTER.IT SPA");
		Assert.assertEquals(res.get("registrar:url"), "http://we.register.it");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS1.REGISTER.IT"));
		Assert.assertTrue(nss.contains("NS2.REGISTER.IT"));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new COMWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.com.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
