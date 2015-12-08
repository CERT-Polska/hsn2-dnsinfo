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

import pl.nask.hsn2.service.parser.CLWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class CLWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new CLWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/allendesmotos.cl.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("changed"), "16 de octubre de 2015 (14:16:10 GMT)");

		Assert.assertEquals(res.get("contacts:owner:name"), "Pablo Allendes Bustos (PABLO ALLENDES BUSTOS)");
		Assert.assertEquals(res.get("contacts:admin:name"), "Pablo Allendes Bustos");
		Assert.assertEquals(res.get("contacts:tech:name"), "oscar Ibáñez Muñoz");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns21.dns-principal-2.com"));
		Assert.assertTrue(nss.contains("ns22.dns-principal-2.com"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new CLWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.cl.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
