package pl.nask.hsn2.tests;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pl.nask.hsn2.service.WhoIsConnectorImpl;

public class WhoIsConnectorImplTest {

	@Test
	public void simpleTest() throws FileNotFoundException {
		WhoIsConnectorImpl connector = new WhoIsConnectorImpl();

		Assert.assertEquals(connector.getWhoisServer(null), null);
		Assert.assertEquals(connector.getWhoisServer(""), null);
		Assert.assertEquals(connector.getWhoisServer("    "), null);

		Assert.assertEquals(connector.getWhoisServer("alibaba.i.40.rozbujnikow.pl"), "whois.dns.pl");
		Assert.assertEquals(connector.getWhoisServer("test.co.bi"), "whois.nic.bi");
		Assert.assertEquals(connector.getWhoisServer("test.mobi"), "whois.dotmobiregistry.net");
		Assert.assertEquals(connector.getWhoisServer("   test.mobi"), "whois.dotmobiregistry.net");
		Assert.assertEquals(connector.getWhoisServer("test.mobi   "), "whois.dotmobiregistry.net");
		Assert.assertEquals(connector.getWhoisServer("   test.co.bi   "), "whois.nic.bi");
		Assert.assertEquals(connector.getWhoisServer("xn----gtbvck1ae7e.xn--p1ai"), "whois.tcinet.ru");
	}
}
