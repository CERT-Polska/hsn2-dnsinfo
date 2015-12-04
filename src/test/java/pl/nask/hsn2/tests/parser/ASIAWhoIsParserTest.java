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

import pl.nask.hsn2.service.parser.ASIAWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class ASIAWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new ASIAWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/hellomagazine.asia.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "09-Sep-2009 17:37:24 UTC");
		Assert.assertEquals(res.get("changed"), "26-Aug-2015 15:51:20 UTC");
		Assert.assertEquals(res.get("expires"), "09-Sep-2017 17:37:24 UTC");

		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> status = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(status.size(), 4);
		Assert.assertTrue(status.contains("clientDeleteProhibited"));
		Assert.assertTrue(status.contains("clientRenewProhibited"));
		Assert.assertTrue(status.contains("clientTransferProhibited"));
		Assert.assertTrue(status.contains("clientUpdateProhibited"));

		Assert.assertEquals(res.get("contacts:owner:handle"), "CR13769584");
		Assert.assertEquals(res.get("contacts:owner:name"), "Hector Ramos");
		Assert.assertEquals(res.get("contacts:owner:organization"), "HOLA S.A.");

		Assert.assertEquals(res.get("contacts:admin:handle"), "CR13769586");
		Assert.assertEquals(res.get("contacts:admin:name"), "Hector Ramos");
		Assert.assertEquals(res.get("contacts:admin:organization"), "HOLA S.A.");

		Assert.assertEquals(res.get("contacts:tech:handle"), "CR13769585");
		Assert.assertEquals(res.get("contacts:tech:name"), "Hector Ramos");
		Assert.assertEquals(res.get("contacts:tech:organization"), "HOLA S.A.");

		Assert.assertEquals(res.get("registrar:name"), "GoDaddy.com, LLC R45-ASIA (146)");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("NS.HOLA.COM"));
		Assert.assertTrue(nss.contains("NS2.HOLA.COM"));
		Assert.assertTrue(nss.contains("NS3.HOLA.COM"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new ASIAWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.asia.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
