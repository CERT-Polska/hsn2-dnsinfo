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

import pl.nask.hsn2.service.parser.BRWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class BRWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new BRWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/uol.com.br.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "19960424 #7137");
		Assert.assertEquals(res.get("expires"), "20230424");
		Assert.assertEquals(res.get("changed"), "20130410");
		Assert.assertEquals(res.get("status"), "published");

		Assert.assertEquals(res.get("contacts:owner:name"), "Universo Online S.A.");
		Assert.assertEquals(res.get("contacts:owner:country"), "BR");
		Assert.assertEquals(res.get("contacts:admin:name"), "Contato da Entidade UOL");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("eliot.uol.com.br 200.221.11.98 "));
		Assert.assertTrue(nss.contains("borges.uol.com.br 200.147.255.105 "));
		Assert.assertTrue(nss.contains("charles.uol.com.br 200.147.38.8 "));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new BRWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.br.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
