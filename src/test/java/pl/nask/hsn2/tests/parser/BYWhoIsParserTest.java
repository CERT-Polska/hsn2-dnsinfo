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

import pl.nask.hsn2.service.parser.BYWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class BYWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new BYWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/replay.by.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);

		Assert.assertFalse(parser.isAvailable(data));

		Assert.assertEquals(res.get("registrar:name"), "Reliable Software, Ltd");
		Assert.assertEquals(res.get("changed"), "2015-08-20");
		Assert.assertEquals(res.get("created"), "2014-05-05");
		Assert.assertEquals(res.get("expires"), "2016-05-05");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("ns1.beget.ru"));
		Assert.assertTrue(nss.contains("ns2.beget.ru"));
		Assert.assertTrue(nss.contains("ns1.beget.pro"));
		Assert.assertTrue(nss.contains("ns2.beget.pro"));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new BYWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.by.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
