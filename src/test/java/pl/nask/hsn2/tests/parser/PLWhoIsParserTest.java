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

import pl.nask.hsn2.service.parser.PLWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class PLWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new PLWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/nask.pl.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("dnssec"), "Unsigned");
		Assert.assertEquals(res.get("expires"), "not defined");
		Assert.assertEquals(res.get("created"), "1995.04.26 13:00:00");
		Assert.assertEquals(res.get("changed"), "2005.04.26 09:55:09");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 6);
		Assert.assertTrue(nss.contains("bilbo.nask.pl"));
		Assert.assertTrue(nss.contains("eomer.nask.pl"));
		Assert.assertTrue(nss.contains("kirdan.nask.pl"));
		Assert.assertTrue(nss.contains("nms.cyf-kr.edu.pl"));
		Assert.assertTrue(nss.contains("ns.tpnet.pl"));
		Assert.assertTrue(nss.contains("zt.piotrkow.tpsa.pl"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new PLWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.pl.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
