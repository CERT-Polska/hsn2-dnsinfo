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

import pl.nask.hsn2.service.parser.IMWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;

public class IMWhoIsParserTest {
	@Test
	public void dataParsingTest() throws IOException {
		WhoIsParser parser = new IMWhoIsParser();

		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/fiesta.im.whois")), StandardCharsets.UTF_8);

		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);

		Assert.assertNotNull(res);
		Assert.assertEquals(res.get("expires"), "03/03/2016 00:59:59");

		Assert.assertEquals(res.get("registrar:name"), "Reseller - Key-Systems GmbH");
		Assert.assertEquals(res.get("contacts:owner:name"), "Whois privacy protection service");
		Assert.assertEquals(res.get("contacts:admin:name"), "Whois privacy protection service");
		Assert.assertEquals(res.get("contacts:tech:name"), "Whois privacy protection service");

		Assert.assertNotNull(res.get("nameservers"));
		Assert.assertTrue(res.get("nameservers") instanceof String);
		List<String> nss = Arrays.asList(res.get("nameservers").split(";"));
		Assert.assertEquals(nss.size(), 2);
		Assert.assertTrue(nss.contains("pid1.srv53.net."));
		Assert.assertTrue(nss.contains("pid2.srv53.org."));

		Assert.assertFalse(parser.isAvailable(data));
	}

	@Test
	public void availableTest() throws IOException {
		WhoIsParser parser = new IMWhoIsParser();
		String data = new String(Files.readAllBytes(Paths.get("src/test/resources/available.im.whois")), StandardCharsets.UTF_8);
		Assert.assertNotNull(data);

		Map<String, String> res = parser.parse(data);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.isEmpty());
		Assert.assertTrue(parser.isAvailable(data));
	}
}
