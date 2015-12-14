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

import pl.nask.hsn2.service.parser.ATWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class ATWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new ATWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/jreopcool.at.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);

		Assert.assertEquals(res.get("changed"), "20150929 16:11:51");

		Assert.assertEquals(res.get("contacts:owner:name"), "Frank Richards");
		Assert.assertEquals(res.get("contacts:owner:handle"), "FR10402049-NICAT");
		Assert.assertEquals(res.get("contacts:admin:name"), "Frank Richards");
		Assert.assertEquals(res.get("contacts:admin:handle"), "FR10402050-NICAT");
		Assert.assertEquals(res.get("contacts:tech:name"), "Frank Richards");
		Assert.assertEquals(res.get("contacts:tech:handle"), "FR10402050-NICAT");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 4);
		Assert.assertTrue(nss.contains("ns1.dirrolkh.at"));
		Assert.assertTrue(nss.contains("ns2.dirrolkh.at"));
		Assert.assertTrue(nss.contains("ns3.dirrolkh.at"));
		Assert.assertTrue(nss.contains("ns4.dirrolkh.at"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new ATWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.at.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
