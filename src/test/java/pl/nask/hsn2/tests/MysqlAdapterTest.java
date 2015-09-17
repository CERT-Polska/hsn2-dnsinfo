package pl.nask.hsn2.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import pl.nask.hsn2.service.MysqlAdapter;

public class MysqlAdapterTest {

	private static String SIMPLE_INSERT_QUERY = "INSERT INTO WhoIsData "
			+ "(rowUpdated, domain, created, updated, expires, registrant, registrar, nss) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	@Test
	public void testCountOfChars() {
		Assert.assertEquals(0, MysqlAdapter.countCharacters('?', null));
		Assert.assertEquals(0, MysqlAdapter.countCharacters('?', ""));
		Assert.assertEquals(0, MysqlAdapter.countCharacters('?', "fsdlkfjdsklfkldsjfkdsjf fldsj ll fjsdlf lsdkj"));
		Assert.assertEquals(4, MysqlAdapter.countCharacters('?', "fsdl?kfjdsklfkld?sjfkdsjf fld?sj ll fjsdlf l?sdkj"));
		Assert.assertEquals(8, MysqlAdapter.countCharacters('?', SIMPLE_INSERT_QUERY));
	}

	@Test
	public void testBuildInsertQuery() {
		List<String> params = new ArrayList<String>();
		params.add("created");
		params.add("expires");
		params.add("registrant");
		
		String insertQuery = MysqlAdapter.buildInsertQuery(params);
		Assert.assertNotNull(insertQuery);
		Assert.assertEquals("INSERT INTO WhoIsData (rowUpdated, domain, created, expires, registrant) VALUES (?, ?, ?, ?, ?)", insertQuery);

		insertQuery = MysqlAdapter.buildInsertQuery("SomeTableName", params);
		Assert.assertNotNull(insertQuery);
		Assert.assertEquals("INSERT INTO SomeTableName (rowUpdated, domain, created, expires, registrant) VALUES (?, ?, ?, ?, ?)", insertQuery);
	}
}
