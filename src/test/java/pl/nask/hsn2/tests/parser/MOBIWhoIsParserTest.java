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

import pl.nask.hsn2.service.parser.MOBIWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class MOBIWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new MOBIWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/wapka.mobi.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "01-Feb-2008 17:57:22 UTC");
		Assert.assertEquals(res.get("expires"), "01-Feb-2017 17:57:22 UTC");
		Assert.assertEquals(res.get("changed"), "13-Aug-2015 07:15:50 UTC");

		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> statuses = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(statuses.size(), 1);
		Assert.assertTrue(statuses.contains("ok"));

		Assert.assertEquals(res.get("contacts:owner:handle"), "322f5XXX14c7be56");
		Assert.assertEquals(res.get("contacts:owner:name"), "WhoisGuard Protected");
		Assert.assertEquals(res.get("contacts:owner:organization"), "WhoisGuard, Inc.");

		Assert.assertEquals(res.get("contacts:admin:handle"), "322f5XXX14c7be56");
		Assert.assertEquals(res.get("contacts:admin:name"), "WhoisGuard Protected");
		Assert.assertEquals(res.get("contacts:admin:organization"), "WhoisGuard, Inc.");

		Assert.assertEquals(res.get("contacts:tech:handle"), "322f557d14c7be56");
		Assert.assertEquals(res.get("contacts:tech:name"), "WhoisGuard Protected");
		Assert.assertEquals(res.get("contacts:tech:organization"), "WhoisGuard, Inc.");

		Assert.assertEquals(res.get("registrar:name"), "eNom, Inc (48)");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 6);
		Assert.assertTrue(nss.contains("NS2.WAPKA.MOBI"));
		Assert.assertTrue(nss.contains("NS1.WAPKA.MOBI"));
		Assert.assertTrue(nss.contains("NS3.WAPKA.MOBI"));
		Assert.assertTrue(nss.contains("NS1.WAPKA.ME"));
		Assert.assertTrue(nss.contains("NS2.WAPKA.ME"));
		Assert.assertTrue(nss.contains("NS3.WAPKA.ME"));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new MOBIWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.mobi.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
