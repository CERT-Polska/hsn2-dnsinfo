package pl.nask.hsn2.tests.parser.idn;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import pl.nask.hsn2.service.parser.WhoIsParser;
import pl.nask.hsn2.service.parser.idn.P1AIWhoIsParser;

public class P1AIWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new P1AIWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/xn----gtbvck1ae7e.xn--p1ai.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("contacts:owner:name"), "LLC 'Module-Tomsk'");
		Assert.assertEquals(res.get("expires"), "2016.11.30");
		Assert.assertEquals(res.get("created"), "2010.11.30");
		Assert.assertEquals(res.get("registrar:id"), "RUCENTER-RF");
		Assert.assertEquals(res.get("status"), "REGISTERED, DELEGATED, VERIFIED");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.tom70.ru."));
		Assert.assertTrue(nss.contains("ns2.tom70.ru."));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new P1AIWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.xn--p1ai.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
