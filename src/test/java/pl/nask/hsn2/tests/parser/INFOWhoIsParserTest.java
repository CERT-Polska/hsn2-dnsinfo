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

import pl.nask.hsn2.service.parser.INFOWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class INFOWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new INFOWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/homemadedollars.info.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2011-08-17T04:27:55Z");
		Assert.assertEquals(res.get("expires"), "2016-08-17T04:27:55Z");
		Assert.assertEquals(res.get("changed"), "2015-09-28T08:00:48Z");
		Assert.assertEquals(res.get("dnssec"), "Unsigned");
		
		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> statuses = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(statuses.size(), 3);
		Assert.assertTrue(statuses.contains("pendingDelete"));
		Assert.assertTrue(statuses.contains("serverHold"));
		Assert.assertTrue(statuses.contains("redemptionPeriod"));

		Assert.assertEquals(res.get("contacts:owner:handle"), "CR90446017");
		Assert.assertEquals(res.get("contacts:owner:name"), "Registration Private");
		Assert.assertEquals(res.get("contacts:owner:organization"), "Domains By Proxy, LLC");

		Assert.assertEquals(res.get("contacts:admin:handle"), "CR90446019");
		Assert.assertEquals(res.get("contacts:admin:name"), "Registration Private");
		Assert.assertEquals(res.get("contacts:admin:organization"), "Domains By Proxy, LLC");

		Assert.assertEquals(res.get("contacts:tech:handle"), "CR90446018");
		Assert.assertEquals(res.get("contacts:tech:name"), "Registration Private");
		Assert.assertEquals(res.get("contacts:tech:organization"), "Domains By Proxy, LLC");

		Assert.assertEquals(res.get("registrar:id"), "146");
		Assert.assertEquals(res.get("registrar:name"), "GoDaddy.com, LLC (R171-LRMS)");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS35.DOMAINCONTROL.COM"));
		Assert.assertTrue(nss.contains("NS36.DOMAINCONTROL.COM"));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new INFOWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.info.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
