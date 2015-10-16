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

import pl.nask.hsn2.service.parser.ORGWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class ORGWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new ORGWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/asociacioncolombianadecienciasbiologicas.org.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2013-05-30T17:36:33Z");
		Assert.assertEquals(res.get("expires"), "2016-05-30T17:36:33Z");
		Assert.assertEquals(res.get("changed"), "2015-09-18T02:11:14Z");
		Assert.assertEquals(res.get("dnssec"), "Unsigned");
		Assert.assertEquals(res.get("status"), "clientTransferProhibited -- http://www.icann.org/epp#clientTransferProhibited");

		Assert.assertEquals(res.get("contacts:owner:handle"), "7e2334065ce17cb5");
		Assert.assertEquals(res.get("contacts:owner:name"), "Ivan Cortes Mata");
		Assert.assertEquals(res.get("contacts:owner:organization"), "ACCB");

		Assert.assertEquals(res.get("contacts:admin:handle"), "7e2334065ce17cb5");
		Assert.assertEquals(res.get("contacts:admin:name"), "Ivan Cortes Mata");
		Assert.assertEquals(res.get("contacts:admin:organization"), "ACCB");

		Assert.assertEquals(res.get("contacts:tech:handle"), "7e2334065ce17cb5");
		Assert.assertEquals(res.get("contacts:tech:name"), "Ivan Cortes Mata");
		Assert.assertEquals(res.get("contacts:tech:organization"), "ACCB");

		Assert.assertEquals(res.get("registrar:id"), "48");
		Assert.assertEquals(res.get("registrar:name"), "eNom, Inc. (R39-LROR)");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS8495.HOSTGATOR.COM"));
		Assert.assertTrue(nss.contains("NS8496.HOSTGATOR.COM"));
		
		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new ORGWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.org.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
