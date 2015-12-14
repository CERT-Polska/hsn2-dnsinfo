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

import pl.nask.hsn2.service.parser.CNWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class CNWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new CNWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/juuprasoon.cn.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);

		Assert.assertEquals(res.get("created"), "2015-11-23 15:29:37");
		Assert.assertEquals(res.get("expires"), "2016-11-23 15:29:37");
		Assert.assertEquals(res.get("dnssec"), "unsigned");

		Assert.assertEquals(res.get("contacts:owner:name"), "广东耐思尼克信息技术有限公司");
		Assert.assertEquals(res.get("contacts:owner:handle"), "nice-2015535500");
		Assert.assertEquals(res.get("registrar:name"), "广东耐思尼克信息技术有限公司（原珠海耐思尼克信息技术有限公司）");

		Assert.assertNotNull(res.get("status"));
		Assert.assertTrue(res.get("status") instanceof String);
		List<String> status = Arrays.asList(res.get("status").split(";"));
		Assert.assertEquals(status.size(), 2);
		Assert.assertTrue(status.contains("clientDeleteProhibited"));
		Assert.assertTrue(status.contains("clientTransferProhibited"));

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns1.kilofrogs.at"));
		Assert.assertTrue(nss.contains("ns2.kilofrogs.at"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new CNWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.cn.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
