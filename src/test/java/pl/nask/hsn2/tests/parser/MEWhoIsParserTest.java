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

import pl.nask.hsn2.service.parser.MEWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class MEWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new MEWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/liam0205.me.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "18-Oct-2013 02:26:25 UTC");
		Assert.assertEquals(res.get("changed"), "24-Jul-2015 01:02:16 UTC");
		Assert.assertEquals(res.get("expires"), "18-Oct-2016 02:26:25 UTC");

		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> status = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(status.size(), 4);
		Assert.assertTrue(status.contains("CLIENT DELETE PROHIBITED"));
		Assert.assertTrue(status.contains("CLIENT RENEW PROHIBITED"));
		Assert.assertTrue(status.contains("CLIENT TRANSFER PROHIBITED"));
		Assert.assertTrue(status.contains("CLIENT UPDATE PROHIBITED"));

		Assert.assertEquals(res.get("contacts:owner:handle"), "CR178273213");
		Assert.assertEquals(res.get("contacts:owner:name"), "Ch'en Meng");
		Assert.assertEquals(res.get("contacts:owner:organization"), "Home");

		Assert.assertEquals(res.get("contacts:admin:handle"), "CR178273214");
		Assert.assertEquals(res.get("contacts:admin:name"), "Ch'en Meng");
		Assert.assertEquals(res.get("contacts:admin:organization"), "Home");

		Assert.assertEquals(res.get("contacts:tech:handle"), "CR178273216");
		Assert.assertEquals(res.get("contacts:tech:name"), "Ch'en Meng");
		Assert.assertEquals(res.get("contacts:tech:organization"), "Home");

		Assert.assertEquals(res.get("registrar:name"), "Wild West Domains, LLC R53-ME (440)");
		Assert.assertEquals(res.get("dnssec"), "Unsigned");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS1.EEDNS.COM"));
		Assert.assertTrue(nss.contains("NS2.EEDNS.COM"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new MEWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.me.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
