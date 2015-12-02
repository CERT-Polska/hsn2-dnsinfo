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

import pl.nask.hsn2.service.parser.PROWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class PROWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new PROWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/flystar.pro.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "10-Feb-2012 07:40:49 UTC");
		Assert.assertEquals(res.get("expires"), "10-Feb-2016 00:00:00 UTC");
		Assert.assertEquals(res.get("changed"), "17-Nov-2015 15:41:37 UTC");
		Assert.assertEquals(res.get("status"), "clientTransferProhibited -- http://www.icann.org/epp#clientTransferProhibited");

		Assert.assertEquals(res.get("contacts:owner:handle"), "SX9MBKX-RU");
		Assert.assertEquals(res.get("contacts:owner:name"), "Alexey Zezegov");
		Assert.assertEquals(res.get("contacts:owner:organization"), "Alexey Zezegov");

		Assert.assertEquals(res.get("contacts:tech:handle"), "SX9MBKX-RU");
		Assert.assertEquals(res.get("contacts:tech:name"), "Alexey Zezegov");
		Assert.assertEquals(res.get("contacts:tech:organization"), "Alexey Zezegov");

		Assert.assertEquals(res.get("contacts:zone:handle"), "SX9MBKX-RU");
		Assert.assertEquals(res.get("contacts:zone:name"), "Alexey Zezegov");
		Assert.assertEquals(res.get("contacts:zone:organization"), "Alexey Zezegov");

		Assert.assertEquals(res.get("contacts:tech:handle"), "SX9MBKX-RU");
		Assert.assertEquals(res.get("contacts:tech:name"), "Alexey Zezegov");
		Assert.assertEquals(res.get("contacts:tech:organization"), "Alexey Zezegov");

		Assert.assertEquals(res.get("registrar:name"), "Regional Network Information Center JSC dba RU-CENTER (R2369-PRO)");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("NS1.JINO.RU"));
		Assert.assertTrue(nss.contains("NS2.JINO.RU"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new PROWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.pro.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
