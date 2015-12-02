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

import pl.nask.hsn2.service.parser.CCWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class CCWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new CCWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/sarov.cc.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);


		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("whoisserver"), "whois.evonames.com");
		Assert.assertEquals(res.get("status"), "ok http://www.icann.org/epp#OK");
		Assert.assertEquals(res.get("created"), "2014-08-18T08:29:28Z");
		Assert.assertEquals(res.get("changed"), "2015-09-10T09:20:16Z");
		Assert.assertEquals(res.get("expires"), "2016-08-18T08:29:28Z");
		Assert.assertEquals(res.get("registrar:id"), "1418");
		Assert.assertEquals(res.get("registrar:name"), "EVOPLUS LTD");
		Assert.assertEquals(res.get("registrar:url"), "http://www.evonames.com");
		Assert.assertEquals(res.get("dnssec"), "unsigned");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("DNS1.RU-TLD.RU"));
		Assert.assertTrue(nss.contains("DNS2.RU-TLD.RU"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new CCWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.cc.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
