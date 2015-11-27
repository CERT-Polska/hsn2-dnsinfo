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

import pl.nask.hsn2.service.parser.KZWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class KZWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new KZWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nic.kz.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertFalse(parser.isAvailable(data));

		Assert.assertEquals(res.get("contacts:owner:name"), "KazNIC");
		Assert.assertEquals(res.get("contacts:owner:organization"), "KazNIC");
		Assert.assertEquals(res.get("contacts:owner:country"), "KZ");

		Assert.assertEquals(res.get("contacts:admin:handle"), "PG134");
		Assert.assertEquals(res.get("contacts:admin:name"), "Gussev, Pavel M.");
		Assert.assertEquals(res.get("contacts:admin:email"), "pg@nic.kz");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("ns.nic.kz"));
		Assert.assertTrue(nss.contains("ns2.nic.kz"));
		Assert.assertTrue(nss.contains("ns.relcom.kz"));

		Assert.assertEquals(res.get("created"), "1999-08-17 20:34:57 (GMT+0:00)");
		Assert.assertEquals(res.get("changed"), "1999-08-17 20:34:57 (GMT+0:00)");
		Assert.assertEquals(res.get("status"), "ok - Normal state.");
		Assert.assertEquals(res.get("registrar:name"), "KAZNIC");
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new KZWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.kz.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
