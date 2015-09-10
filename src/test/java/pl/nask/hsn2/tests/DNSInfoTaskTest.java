package pl.nask.hsn2.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.testng.annotations.Test;

import pl.nask.hsn2.InputDataException;
import pl.nask.hsn2.ParameterException;
import pl.nask.hsn2.ResourceException;
import pl.nask.hsn2.StorageException;
import pl.nask.hsn2.TaskContext;
import pl.nask.hsn2.service.DNSInfoTask;
import pl.nask.hsn2.wrappers.ObjectDataWrapper;
import pl.nask.hsn2.wrappers.ParametersWrapper;
import mockit.Mocked;
import mockit.NonStrictExpectations;

public class DNSInfoTaskTest {

	@Mocked
	TaskContext jobContext;

	@Mocked
	ParametersWrapper parameters;

	@Mocked
	ObjectDataWrapper data;

	private void setUpMocks() throws IOException {
	}

	private void setUpExpectations(final boolean collectStats, final String urlDomain)
			throws ParameterException, StorageException, ResourceException {
		new NonStrictExpectations() {
			{
				parameters.getBoolean("collect_stats", false); result = collectStats;
				parameters.get("url_domain_key", "url_domain"); result = "url_domain";
				parameters.get("keys_map", anyString); result = "created->whois_domain_create";
				data.getString("url_domain"); result = urlDomain;
			}
		};
	}

	@Test
	public void simpleTest() throws ParameterException, StorageException, ResourceException, InputDataException, IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		setUpMocks();
		setUpExpectations(false, "tt.materassieretisardegna.it");
//		
//		DNSInfoTask task = new DNSInfoTask(jobContext, parameters, data, "dnsinfo-zones.txt", "dnsinfo-whois-servers.txt");
//		
//		task.process();
	}
	
	@Test
	public void keysMapParsingTest() throws ParameterException {
		Map<String, String> map = new HashMap<String, String>();
		String string = "created->whois_created    ;    \n	expires->whois_exp";
		DNSInfoTask.parseKeyMaps(map, string);
		Assert.assertEquals(map.size(), 2);
		Assert.assertTrue(map.containsKey("created"));
		Assert.assertTrue(map.containsKey("expires"));
		Assert.assertEquals(map.get("created"), "whois_created");
		Assert.assertEquals(map.get("expires"), "whois_exp");
	}
}
