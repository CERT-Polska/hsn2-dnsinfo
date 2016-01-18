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

import pl.nask.hsn2.service.parser.LTWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class LTWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new LTWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nic.lt.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2002-01-08");
		Assert.assertEquals(res.get("status"), "registered");

		Assert.assertEquals(res.get("registrar:name"), "Kauno technologijos universitetas");
		Assert.assertEquals(res.get("registrar:url"), "http://domains.lt");
		Assert.assertEquals(res.get("registrar:email"), "hostmaster@domreg.lt");

		Assert.assertEquals(res.get("contacts:owner:organization"), "Kauno technologijos universitetas");
		Assert.assertEquals(res.get("contacts:owner:email"), "hostmaster@domreg.lt");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("nemunas.sc-uni.ktu.lt"));
		Assert.assertTrue(nss.contains("ns2.domreg.lt"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new LTWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.lt.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
