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

import pl.nask.hsn2.service.parser.USWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class USWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new USWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/gotrubs.us.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "pendingDelete");
		Assert.assertEquals(res.get("created"), "Mon Aug 27 06:18:17 GMT 2012");
		Assert.assertEquals(res.get("changed"), "Wed Oct 07 08:11:56 GMT 2015");
		Assert.assertEquals(res.get("expires"), "Wed Aug 26 23:59:59 GMT 2015");
		Assert.assertEquals(res.get("contacts:owner:handle"), "CR12250XX12");
		Assert.assertEquals(res.get("contacts:owner:name"), "Ri Murr");
		Assert.assertEquals(res.get("contacts:admin:handle"), "CR12250XX14");
		Assert.assertEquals(res.get("contacts:admin:name"), "Ri Murr");
		Assert.assertEquals(res.get("contacts:tech:handle"), "CR12250XX13");
		Assert.assertEquals(res.get("contacts:tech:name"), "Ri Murr");
		Assert.assertEquals(res.get("registrar:id"), "146");
		Assert.assertEquals(res.get("registrar:name"), "GoDaddy.com, Inc.");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS19.DOMAINCONTROL.COM"));
		Assert.assertTrue(nss.contains("NS20.DOMAINCONTROL.COM"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new USWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.us.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
