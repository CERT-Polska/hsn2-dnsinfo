package pl.nask.hsn2.tests.parser;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.nask.hsn2.service.parser.KEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new KEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/tembeaafrica.co.ke.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("changed"), "2015-05-12T08:24:03.043Z");
		Assert.assertEquals(res.get("created"), "2012-06-26T09:32:26.995Z");
		Assert.assertEquals(res.get("expires"), "2016-06-26T09:32:27.007Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "ok");
		Assert.assertEquals(res.get("registrar:name"), "WEB TRIBE LIMITED");

		Assert.assertEquals(res.get("contacts:owner:handle"), "72409-KENIC");
		Assert.assertEquals(res.get("contacts:owner:name"), "Winnie Maru");
		Assert.assertEquals(res.get("contacts:owner:organization"), "Tembea Africa Tours &Travel");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.imagi-ne.com"));
		Assert.assertTrue(nss.contains("ns2.imagi-ne.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new KEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.ke.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
