package pl.nask.hsn2.tests.parser;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.nask.hsn2.service.parser.PTWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PTWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new PTWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/movifornos.pt.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "30/01/2009");
		Assert.assertEquals(res.get("expires"), "30/01/2016");
		Assert.assertEquals(res.get("status"), "ACTIVE");

		Assert.assertEquals(res.get("contacts:owner:name"), "Movifornos - Comercio de Moveis Lda");
		Assert.assertEquals(res.get("contacts:tech:name"), "Carlos Fernando Pereira Pais");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns55.domaincontrol.com."));
		Assert.assertTrue(nss.contains("ns56.domaincontrol.com."));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new PTWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.pt.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
