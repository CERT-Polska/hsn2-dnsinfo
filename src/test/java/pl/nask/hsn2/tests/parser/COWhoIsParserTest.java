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

import pl.nask.hsn2.service.parser.COWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class COWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new COWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/blackfriday-atendimento.co.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("dnssec"), "false");
		Assert.assertEquals(res.get("status"), "clientTransferProhibited");
		Assert.assertEquals(res.get("created"), "Wed Aug 05 16:05:00 GMT 2015");
		Assert.assertEquals(res.get("changed"), "Thu Oct 29 15:25:07 GMT 2015");
		Assert.assertEquals(res.get("expires"), "Thu Aug 04 23:59:59 GMT 2016");

		Assert.assertEquals(res.get("contacts:owner:handle"), "OMB1422620699");
		Assert.assertEquals(res.get("contacts:owner:name"), "On behalf of blackfriday-atendimento.co owner");
		Assert.assertEquals(res.get("contacts:owner:organization"), "c/o whoisproxy.com Ltd.");

		Assert.assertEquals(res.get("contacts:admin:handle"), "OXB1377146682");
		Assert.assertEquals(res.get("contacts:admin:name"), "On behalf of blackfriday-atendimento.co admin");
		Assert.assertEquals(res.get("contacts:admin:organization"), "c/o whoisproxy.com Ltd.");

		Assert.assertEquals(res.get("contacts:tech:handle"), "OFB1455344818");
		Assert.assertEquals(res.get("contacts:tech:name"), "On behalf of blackfriday-atendimento.co tech");
		Assert.assertEquals(res.get("contacts:tech:organization"), "c/o whoisproxy.com Ltd.");

		Assert.assertEquals(res.get("registrar:id"), "269");
		Assert.assertEquals(res.get("registrar:name"), "KEY-SYSTEMS GMBH");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS1.BODIS.COM"));
		Assert.assertTrue(nss.contains("NS2.BODIS.COM"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new COWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.co.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
