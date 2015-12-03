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

import pl.nask.hsn2.service.parser.MXWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class MXWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new MXWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/goalzero.mx.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "2012-02-17");
		Assert.assertEquals(res.get("changed"), "2015-02-12");
		Assert.assertEquals(res.get("expires"), "2016-02-17");

		Assert.assertEquals(res.get("registrar:name"), "Akky (Una division de NIC Mexico)");

		Assert.assertEquals(res.get("contacts:owner:name"), "ROBERTO CEJUDO MARTINEZ");
		Assert.assertEquals(res.get("contacts:owner:city"), "Mexico");

		Assert.assertEquals(res.get("contacts:admin:name"), "Miguel Alejandro Diaz Palmerin");
		Assert.assertEquals(res.get("contacts:admin:city"), "Atizapan de Zaragoza");

		Assert.assertEquals(res.get("contacts:tech:name"), "Miguel Alejandro Diaz Palmerin");
		Assert.assertEquals(res.get("contacts:tech:city"), "Atizapan de Zaragoza");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns597.hostgator.com"));
		Assert.assertTrue(nss.contains("ns598.hostgator.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new MXWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.mx.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
