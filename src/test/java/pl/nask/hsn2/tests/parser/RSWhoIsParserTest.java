package pl.nask.hsn2.tests.parser;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.nask.hsn2.service.parser.RSWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RSWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new RSWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/coffeedream.rs.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "26.09.2008 12:40:51");
		Assert.assertEquals(res.get("changed"), "31.08.2015 08:52:12");
		Assert.assertEquals(res.get("expires"), "26.09.2016 12:40:51");
		Assert.assertEquals(res.get("status"), "Active");
		Assert.assertEquals(res.get("registrar:name"), "BeotelNet-ISP d.o.o.");

		Assert.assertEquals(res.get("contacts:owner:handle"), "20253738");
		Assert.assertEquals(res.get("contacts:owner:name"), "Santa clara d.o.o.");

		Assert.assertEquals(res.get("contacts:admin:name"), "Milos Radivojevic");
		Assert.assertEquals(res.get("contacts:tech:name"), "Drazen Milosevic, Verat doo");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("hns1.verat.net."));
		Assert.assertTrue(nss.contains("hns2.verat.net."));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new RSWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.rs.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
