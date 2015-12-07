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

import pl.nask.hsn2.service.parser.SEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class SEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new SEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nobelprize.se.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2003-04-15");
		Assert.assertEquals(res.get("changed"), "2015-02-01");
		Assert.assertEquals(res.get("expires"), "2016-04-15");
		Assert.assertEquals(res.get("status"), "ok");
		Assert.assertEquals(res.get("dnssec"), "unsigned delegation");

		Assert.assertEquals(res.get("registrar:name"), "Domaininfo AB");
		Assert.assertEquals(res.get("contacts:owner:handle"), "domnob1112-00001");
		Assert.assertEquals(res.get("contacts:admin:handle"), "dipcon0903-00050");
		Assert.assertEquals(res.get("contacts:tech:handle"), "dipcon0903-00050");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("dns04.ports.net"));
		Assert.assertTrue(nss.contains("dns03.ports.se"));
		Assert.assertTrue(nss.contains("dns02.ports.se"));
		Assert.assertTrue(nss.contains("dns01.dipcon.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new SEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.se.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
