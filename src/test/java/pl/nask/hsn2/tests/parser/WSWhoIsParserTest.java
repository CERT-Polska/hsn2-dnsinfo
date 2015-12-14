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

import pl.nask.hsn2.service.parser.WSWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class WSWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new WSWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/datec.com.ws.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);

		Assert.assertEquals(res.get("created"), "2009-01-20");
		Assert.assertEquals(res.get("changed"), "2015-11-03");
		Assert.assertEquals(res.get("expires"), "2016-01-20");
		Assert.assertEquals(res.get("status"), "ok");
		Assert.assertEquals(res.get("dnssec"), "unsigned");

		Assert.assertEquals(res.get("registrar:name"), "Computer Services Limited");
		Assert.assertEquals(res.get("contacts:owner:name"), "Datec Samoa Limited");
		Assert.assertEquals(res.get("contacts:admin:name"), "Datec Samoa Limited");
		Assert.assertEquals(res.get("contacts:tech:name"), "Datec Samoa Limited");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.secure.net"));
		Assert.assertTrue(nss.contains("ns2.secure.net"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new WSWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ws.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
