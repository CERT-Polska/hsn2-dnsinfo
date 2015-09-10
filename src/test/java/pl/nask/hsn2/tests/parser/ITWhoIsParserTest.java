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

import pl.nask.hsn2.service.parser.ITWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class ITWhoIsParserTest {

	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new ITWhoIsParser();
		
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/unica.it.whois")), StandardCharsets.UTF_8);
		
		Assert.assertNotNull(data);
		
		
		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("status"), "ok");
		Assert.assertEquals(res.get("created"), "1996-01-29 00:00:00");
		Assert.assertEquals(res.get("changed"), "2015-07-14 18:43:56");
		Assert.assertEquals(res.get("expires"), "2016-01-29");
		Assert.assertEquals(res.get("contacts:owner:organization"), "Universita' degli Studi di Cagliari");
		Assert.assertEquals(res.get("contacts:owner:created"), "2007-03-01 10:47:03");
		Assert.assertEquals(res.get("contacts:owner:changed"), "2011-03-24 11:01:07");
		Assert.assertEquals(res.get("contacts:owner:address"), "Via Ospedale 72\n                    Cagliari\n                    09124\n                    CA\n                    IT\n  ");
		Assert.assertEquals(res.get("contacts:admin:name"), "Gaetano Melis");
		Assert.assertEquals(res.get("contacts:admin:organization"), "Universita' degli Studi di Cagliari");
		Assert.assertEquals(res.get("contacts:tech:name"), "Roberto Porcu;Stefano Manfredda;Vincenzo Errante");
		Assert.assertEquals(res.get("contacts:tech:organization"), "Universita' degli Studi di Cagliari;Universita' degli Studi di Cagliari;Universita' degli Studi di Cagliari");
		Assert.assertEquals(res.get("registrar:id"), "GARR-REG");
		Assert.assertEquals(res.get("registrar:name"), "Consortium GARR");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 3);
		Assert.assertTrue(nss.contains("vaxca1.unica.it"));
		Assert.assertTrue(nss.contains("vaxca3.unica.it"));
		Assert.assertTrue(nss.contains("ns1.garr.net"));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new ITWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.it.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);
		
		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
