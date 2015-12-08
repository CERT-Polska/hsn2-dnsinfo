package pl.nask.hsn2.tests;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import pl.nask.hsn2.service.extractors.DomainExtractor;
import pl.nask.hsn2.service.extractors.ZonesBasedDomainExtractor;

public class ZonesBasedDomainExtractorTest {

	@Test
	public void simpleTest() throws FileNotFoundException {
		DomainExtractor extractor = new ZonesBasedDomainExtractor("dnsinfo-zones.txt");

		Assert.assertEquals(extractor.getDomain(null), null);
		Assert.assertEquals(extractor.getDomain(""), null);
		Assert.assertEquals(extractor.getDomain("dsadasd.milzano.bs.it"), "dsadasd.milzano.bs.it");
		Assert.assertEquals(extractor.getDomain("tyt..hyh.dss.it"), "dss.it");
		Assert.assertEquals(extractor.getDomain(".dsadasd.milzano.bs.it"), "dsadasd.milzano.bs.it");
		Assert.assertEquals(extractor.getDomain("milzano.bs.it"), null);
		Assert.assertEquals(extractor.getDomain("wp.pl"), "wp.pl");
		Assert.assertEquals(extractor.getDomain("www.xn----gtbvck1ae7e.xn--p1ai"), "xn----gtbvck1ae7e.xn--p1ai");
	}
}
