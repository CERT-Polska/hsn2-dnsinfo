package pl.nask.hsn2.tests;

import org.testng.annotations.Test;

import pl.nask.hsn2.service.CachedWhoIsConnector;
import pl.nask.hsn2.service.WhoIsConnector;
import mockit.Mocked;
import mockit.NonStrictExpectations;

public class CachedWhoIsConnectorTest {

	@Mocked
	WhoIsConnector whoisConnector;

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void badConstructorArgument() {
		new CachedWhoIsConnector(null, 0, 0);
	}
	
	@Test
	public void simpleTest() {
		new NonStrictExpectations() {
			{
				whoisConnector.getWhoisData(anyString); result = "some data";
			}
		};
		

		CachedWhoIsConnector connector = new CachedWhoIsConnector(whoisConnector, 1, 6);
		
		for (int i=0 ; i < 50 ; i++) {
			connector.getWhoisData(""+(int)(Math.random()*15));
			try {
				Thread.sleep((int)(Math.random()*200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void staticTest() {
		new NonStrictExpectations() {
			{
				whoisConnector.getWhoisData(anyString); result = "some data";
			}
		};
		for (int i=0 ; i < 50 ; i++) {
			CachedWhoIsConnector connector = new CachedWhoIsConnector(whoisConnector, 1, 6);
			connector.getWhoisData(""+(int)(Math.random()*15));
			try {
				Thread.sleep((int)(Math.random()*200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
}
