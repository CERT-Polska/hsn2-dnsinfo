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

import pl.nask.hsn2.service.parser.XYZWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class XYZWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new XYZWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/immortal111.xyz.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2015-09-10T18:02:20.0Z");
		Assert.assertEquals(res.get("expires"), "2016-09-10T23:59:59.0Z");
		Assert.assertEquals(res.get("changed"), "2015-09-18T15:14:39.0Z");
		Assert.assertEquals(res.get("dnssec"), "unsigned");
		Assert.assertEquals(res.get("status"), "clientTransferProhibited");

		Assert.assertEquals(res.get("contacts:owner:handle"), "C29026376-CNIC");
		Assert.assertEquals(res.get("contacts:owner:name"), "WhoisGuard Protected");
		Assert.assertEquals(res.get("contacts:owner:organization"), "WhoisGuard, Inc.");

		Assert.assertEquals(res.get("contacts:admin:handle"), "C29026363-CNIC");
		Assert.assertEquals(res.get("contacts:admin:name"), "WhoisGuard Protected");
		Assert.assertEquals(res.get("contacts:admin:organization"), "WhoisGuard, Inc.");

		Assert.assertEquals(res.get("contacts:tech:handle"), "C29026379-CNIC");
		Assert.assertEquals(res.get("contacts:tech:name"), "WhoisGuard Protected");
		Assert.assertEquals(res.get("contacts:tech:organization"), "WhoisGuard, Inc.");

		Assert.assertEquals(res.get("registrar:id"), "1068");
		Assert.assertEquals(res.get("registrar:name"), "Namecheap");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("NS1.DERALTS.AT"));
		Assert.assertTrue(nss.contains("NS2.DERALTS.AT"));
		Assert.assertTrue(nss.contains("NS3.DERALTS.AT"));
		Assert.assertTrue(nss.contains("NS4.DERALTS.AT"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new XYZWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.xyz.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
