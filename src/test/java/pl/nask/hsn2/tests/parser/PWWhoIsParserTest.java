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

import pl.nask.hsn2.service.parser.PWWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class PWWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new PWWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nom.pw.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2015-07-20T11:38:06.0Z");
		Assert.assertEquals(res.get("expires"), "2016-07-20T23:59:59.0Z");
		Assert.assertEquals(res.get("changed"), "2015-07-25T11:42:14.0Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "clientTransferProhibited");

		Assert.assertEquals(res.get("contacts:owner:handle"), "DI_32718110");
		Assert.assertEquals(res.get("contacts:owner:name"), "XYZ Corporation");
		Assert.assertEquals(res.get("contacts:owner:organization"), "XYZ Corporation");

		Assert.assertEquals(res.get("contacts:admin:handle"), "DI_32718110");
		Assert.assertEquals(res.get("contacts:admin:name"), "XYZ Corporation");
		Assert.assertEquals(res.get("contacts:admin:organization"), "XYZ Corporation");

		Assert.assertEquals(res.get("contacts:tech:handle"), "DI_32718110");
		Assert.assertEquals(res.get("contacts:tech:name"), "XYZ Corporation");
		Assert.assertEquals(res.get("contacts:tech:organization"), "XYZ Corporation");

		Assert.assertEquals(res.get("registrar:id"), "303");
		Assert.assertEquals(res.get("registrar:name"), "PDR Ltd. d/b/a PublicDomainRegistry.com");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("X1.JJPUTX.COM"));
		Assert.assertTrue(nss.contains("X2.JJPUTX.COM"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new PWWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.pw.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
