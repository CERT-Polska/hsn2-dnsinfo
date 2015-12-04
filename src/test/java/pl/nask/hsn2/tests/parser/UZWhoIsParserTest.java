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

import pl.nask.hsn2.service.parser.UZWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class UZWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new UZWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/cctld.uz.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "01-May-2005");
		Assert.assertEquals(res.get("changed"), "01-may-2005");
		Assert.assertEquals(res.get("expires"), "-");
		Assert.assertEquals(res.get("status"), "RESERVED");

		Assert.assertEquals(res.get("contacts:admin:name"), "Center UZINFOCOM");
		Assert.assertEquals(res.get("contacts:tech:name"), "Center UZINFOCOM");
		Assert.assertEquals(res.get("registrar:name"), "UZINFOCOM");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns.uz."));
		Assert.assertTrue(nss.contains("ns2.uz."));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new UZWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.uz.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
