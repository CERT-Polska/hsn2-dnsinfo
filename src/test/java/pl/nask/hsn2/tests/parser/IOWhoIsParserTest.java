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

import pl.nask.hsn2.service.parser.IOWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class IOWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new IOWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/github.io.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("expires"), "2016-03-08");
		Assert.assertEquals(res.get("status"), "Client Updt Lock");
		Assert.assertEquals(res.get("contacts:owner:name"), "Domain Administrator;GitHub, Inc.;548 4th Street;San Francisco;CA;US");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("ns1.p16.dynect.net"));
		Assert.assertTrue(nss.contains("ns2.p16.dynect.net"));
		Assert.assertTrue(nss.contains("ns3.p16.dynect.net"));
		Assert.assertTrue(nss.contains("ns4.p16.dynect.net"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new IOWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.io.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
