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

import pl.nask.hsn2.service.parser.BIZWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class BIZWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new BIZWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nashregion.biz.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("dnssec"), "false");
		Assert.assertEquals(res.get("status"), "ok");
		Assert.assertEquals(res.get("created"), "Mon Feb 28 07:41:57 GMT 2011");
		Assert.assertEquals(res.get("changed"), "Sat Jul 18 15:21:21 GMT 2015");
		Assert.assertEquals(res.get("expires"), "Sat Feb 27 23:59:59 GMT 2016");

		Assert.assertEquals(res.get("contacts:owner:handle"), "CO922475-RT");
		Assert.assertEquals(res.get("contacts:owner:name"), "Arnold Avetisyan");
		Assert.assertEquals(res.get("contacts:owner:organization"), "VIKRAM, LTD");

		Assert.assertEquals(res.get("contacts:admin:handle"), "CA922475-RT");
		Assert.assertEquals(res.get("contacts:admin:name"), "Arnold Avetisyan");
		Assert.assertEquals(res.get("contacts:admin:organization"), "VIKRAM, LTD");

		Assert.assertEquals(res.get("contacts:tech:handle"), "CT922475-RT");
		Assert.assertEquals(res.get("contacts:tech:name"), "Arnold Avetisyan");
		Assert.assertEquals(res.get("contacts:tech:organization"), "VIKRAM, LTD");

		Assert.assertEquals(res.get("registrar:id"), "1362");
		Assert.assertEquals(res.get("registrar:name"), "REGTIME LTD.");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("NS1.TIMEWEB.RU"));
		Assert.assertTrue(nss.contains("NS2.TIMEWEB.RU"));
		Assert.assertTrue(nss.contains("NS3.TIMEWEB.ORG"));
		Assert.assertTrue(nss.contains("NS4.TIMEWEB.ORG"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new BIZWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.biz.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
