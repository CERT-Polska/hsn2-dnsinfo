package pl.nask.hsn2.tests.parser;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.nask.hsn2.service.parser.COMWhoIsParser;
import pl.nask.hsn2.service.parser.EDUWhoIsParser;
import pl.nask.hsn2.service.parser.EUWhoIsParser;
import pl.nask.hsn2.service.parser.ITWhoIsParser;
import pl.nask.hsn2.service.parser.NETWhoIsParser;
import pl.nask.hsn2.service.parser.PLWhoIsParser;
import pl.nask.hsn2.service.parser.WhoIsParser;
import pl.nask.hsn2.service.parser.WhoisParserFactory;

public class WhoisParserFactoryTest {

	@BeforeMethod
	public void clearParsersMap() {
		WhoisParserFactory.clearParsersMap();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void emptyArgumentTest1() {
		WhoisParserFactory.getParser("");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void emptyArgumentTest2() {
		WhoisParserFactory.getParser(null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void emptyArgumentTest3() {
		WhoisParserFactory.getParser("....");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void emptyArgumentTest4() {
		WhoisParserFactory.getParser(".");
	}

	@Test
	public void onlyZoneTest() {
		WhoIsParser parser = WhoisParserFactory.getParser("pl");
		Assert.assertTrue(parser instanceof PLWhoIsParser);
		
		parser = WhoisParserFactory.getParser("pl.");
		Assert.assertTrue(parser instanceof PLWhoIsParser);

		parser = WhoisParserFactory.getParser("pl....");
		Assert.assertTrue(parser instanceof PLWhoIsParser);

		parser = WhoisParserFactory.getParser(".pl.");
		Assert.assertTrue(parser instanceof PLWhoIsParser);

		parser = WhoisParserFactory.getParser("...pl..");
		Assert.assertTrue(parser instanceof PLWhoIsParser);
	}

	@Test
	public void domainTest() {
		WhoIsParser parser = WhoisParserFactory.getParser("sadas.pl");
		Assert.assertTrue(parser instanceof PLWhoIsParser);

		parser = WhoisParserFactory.getParser("dasdas.sadas.pl");
		Assert.assertTrue(parser instanceof PLWhoIsParser);

		parser = WhoisParserFactory.getParser("dasdas.sadas.pl.");
		Assert.assertTrue(parser instanceof PLWhoIsParser);

		parser = WhoisParserFactory.getParser("dasdas.sadas.pl...");
		Assert.assertTrue(parser instanceof PLWhoIsParser);
	}

	@Test
	public void allDomainsTest() {
		WhoIsParser parser = WhoisParserFactory.getParser("sadas.pl");
		Assert.assertTrue(parser instanceof PLWhoIsParser);
		
		parser = WhoisParserFactory.getParser("sadas.com.");
		Assert.assertTrue(parser instanceof COMWhoIsParser);
		
		parser = WhoisParserFactory.getParser("sadas.edu");
		Assert.assertTrue(parser instanceof EDUWhoIsParser);
		
		parser = WhoisParserFactory.getParser("sadas.it..");
		Assert.assertTrue(parser instanceof ITWhoIsParser);
		
		parser = WhoisParserFactory.getParser("sadas.net");
		Assert.assertTrue(parser instanceof NETWhoIsParser);

		parser = WhoisParserFactory.getParser("sadas...das.eu.");
		Assert.assertTrue(parser instanceof EUWhoIsParser);
	}

	@Test
	public void unknownDomainTest() {
		WhoIsParser parser = WhoisParserFactory.getParser("sadas.dfsf");
		Assert.assertNull(parser);
	}

	@Test
	public void reuseTest() {
		//should be clear b'coz @BeforeMethod
		Assert.assertTrue(WhoisParserFactory.getParsersMap().isEmpty());
		
		Assert.assertNotNull(WhoisParserFactory.getParser("sadas.pl"));
		Assert.assertEquals(WhoisParserFactory.getParsersMap().size(), 1);
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("pl"));

		Assert.assertNotNull(WhoisParserFactory.getParser("sadsdasdsaas.pl"));
		Assert.assertEquals(WhoisParserFactory.getParsersMap().size(), 1);
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("pl"));

		Assert.assertNotNull(WhoisParserFactory.getParser("sadsdasdsaas.eu"));
		Assert.assertEquals(WhoisParserFactory.getParsersMap().size(), 2);
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("pl"));
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("eu"));

		Assert.assertNotNull(WhoisParserFactory.getParser("sadsdasdsaas.com"));
		Assert.assertEquals(WhoisParserFactory.getParsersMap().size(), 3);
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("pl"));
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("eu"));
		Assert.assertTrue(WhoisParserFactory.getParsersMap().containsKey("com"));
	}
}
