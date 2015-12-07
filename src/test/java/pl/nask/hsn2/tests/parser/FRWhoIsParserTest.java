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

import pl.nask.hsn2.service.parser.FRWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class FRWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new FRWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/st-jacques.fr.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("created"), "24/09/2010");
		Assert.assertEquals(res.get("changed"), "24/09/2015");
		Assert.assertEquals(res.get("expires"), "24/09/2016");
		Assert.assertEquals(res.get("status"), "ACTIVE");
		Assert.assertEquals(res.get("registrar:name"), "ADISTA");

		Assert.assertEquals(res.get("contacts:owner:name"), "COMMUNE DE SAINT JACQUES DE LA LANDE");
		Assert.assertEquals(res.get("contacts:admin:name"), "Gilles Garry");

		Assert.assertNotNull(res.get("contacts:tech:name"));
		Assert.assertTrue(res.get("contacts:tech:name") instanceof String);
		List<String> tech = Arrays.asList(res.get("contacts:tech:name").split(";"));
		Assert.assertEquals(tech.size(), 2);
		Assert.assertTrue(tech.contains("Pascal Caumont"));
		Assert.assertTrue(tech.contains("Jérôme Boebion"));

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("ns3.rmi.fr"));
		Assert.assertTrue(nss.contains("ns4.rmi.fr"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new FRWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.fr.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
