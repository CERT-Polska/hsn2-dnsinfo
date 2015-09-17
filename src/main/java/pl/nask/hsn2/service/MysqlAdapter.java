package pl.nask.hsn2.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.nask.hsn2.ResourceException;

/**
 * This class is NOT thread safe!
 *
 */
public final class MysqlAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlAdapter.class);

	public static final String DEFAULT_TABLE_NAME = "WhoIsData";

//	private static final String QUERY_SELECT_DATA_RETENTION = "SELECT data_retention "
//			+ "FROM conf WHERE ID = 1";

	private final String mysqlUrl;
	private final String mysqlDb;
	private final String mysqlUser;
	private final String mysqlPassword;

	private Connection conn = null;
	private PreparedStatement statementInsertWhoIsData = null;

	public MysqlAdapter(String url, String database, String user, String password) {
		this.mysqlUrl = url;
		this.mysqlDb = database;
		this.mysqlUser = user;
		this.mysqlPassword = password;
		
		LOGGER.info("Adapter has been created for connection to: {}", this.mysqlUrl);
	}

	public void connect() {
		if (conn != null) {
			throw new IllegalStateException("Connection already created!");
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + mysqlUrl, mysqlUser, mysqlPassword);
			conn.setCatalog(this.mysqlDb);
//			statementInsertWhoIsData = conn.prepareStatement(DEFAULT_QUERY_INSERT);
		} catch (SQLException e) {
			LOGGER.error("Connection error: ", e);
			throw new IllegalStateException("Connection error", e);
		}
	}

	/**
	 * Use reconnect if setting parameters throws an exception.
	 * Try to reconnect and set them again.
	 */
	public void reconnect() {
		close();
		connect();
	}

	private void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("Failed to close sql connection", e);
			}
		}
		conn = null;
		statementInsertWhoIsData = null;
	}

	public static String buildInsertQuery(List<String> parameters) {
		return buildInsertQuery(DEFAULT_TABLE_NAME, parameters);
	}
	
	public static String buildInsertQuery(String tableName, List<String> parameters) {
		if (parameters == null) {
			throw new IllegalArgumentException("Pramaters list cannot be null.");
		}
		StringBuffer queryBuffer = new StringBuffer("INSERT INTO ");
		queryBuffer.append(tableName);
		queryBuffer.append(" (rowUpdated, domain");
		for (String token : parameters) {
			queryBuffer.append(", ");
			queryBuffer.append(token);
		}
		queryBuffer.append(") VALUES (?, ?");
		for (int i=0; i<parameters.size(); i++) {
			queryBuffer.append(", ?");
		}
		queryBuffer.append(")");
		return queryBuffer.toString();
	}

	public final void setupInsertStatement(final List<String> parameters) {
		setupInsertStatement(DEFAULT_TABLE_NAME, parameters);
	}

	public final void setupInsertStatement(String tableName, final List<String> parameters) {
		if (tableName == null || "".equals(tableName)) {
			tableName = DEFAULT_TABLE_NAME;
		}
		String insertQuery = buildInsertQuery(tableName, parameters);
		
		try {
			if (this.statementInsertWhoIsData != null) {
				this.statementInsertWhoIsData.close();
			}
			this.statementInsertWhoIsData = conn.prepareStatement(insertQuery);
		} catch (SQLException e) {
			LOGGER.error("Cannot setup statement.", e);
		}
	}
	
	public final void persistWhoisInfo(String domain, Callable<String> callable) throws ResourceException {
		try {
			int index = 1;
			statementInsertWhoIsData.clearParameters();
			statementInsertWhoIsData.setLong(index++, System.currentTimeMillis() / 1000);
			statementInsertWhoIsData.setString(index++, domain);
			String value = null;
			while (true) {
				try {
					value = callable.call();
					statementInsertWhoIsData.setString(index++, value);
				} catch (Exception e) {
					break;
				}
			}
			statementInsertWhoIsData.execute();
		} catch (SQLException e) {
			throw new ResourceException("Problem during persist operation!", e);
		}
	}
	
//	public long persistHostInfo(HostInfo hostInfo) throws SQLException {
//		int index = 1;
//		long id = 0;
//		statementInsertHostInfo.clearParameters();
//		statementInsertHostInfo.setString(index++, hostInfo.getIpAddress());
//		statementInsertHostInfo.setString(index++, hostInfo.getAsn());
//		statementInsertHostInfo.setString(index++, hostInfo.getCityName());
//		statementInsertHostInfo.setString(index++, hostInfo.getCountryCode());
//		statementInsertHostInfo.setString(index++, hostInfo.getLocationLongitude());
//		statementInsertHostInfo.setString(index++, hostInfo.getLocationLatitude());
//		statementInsertHostInfo.execute();
//		try (ResultSet ids = statementInsertHostInfo.getGeneratedKeys()) {
//			if (ids.next()) {
//				id = ids.getLong(1);
//			} else {
//				throw new SQLException("Failed when inserting hostInfo - no id");
//			}
//		}
//		return id;
//	}

//	public static void setHostInfoId(PreparedStatement statement, int index, long value) throws SQLException {
//		if (value > 0) {
//			statement.setLong(index, value);
//		} else {
//			statement.setNull(index, java.sql.Types.BIGINT);
//		}
//	}

	public boolean attempt(Callable<Void> callable) {
		boolean success = false;
		try {
			if (!conn.isValid(0)) {
				MysqlAdapter.LOGGER.info("Invalid MySQL connection - reconnecting...");
				reconnect();
			}
		} catch (Exception exc) {
			MysqlAdapter.LOGGER.error("Failed reconnect", exc);
			return false;
		}
		try {
			this.conn.setAutoCommit(false);
			callable.call();
			this.conn.commit();
			success = true;
		} catch (Exception exc) {
			MysqlAdapter.LOGGER.error("Failed attempt", exc);
			this.reconnect();
		}
		if (!success) {
			try {
				this.conn.setAutoCommit(false);
				callable.call();
				this.conn.commit();
			} catch (Exception exc) {
				MysqlAdapter.LOGGER.error("Failed retry", exc);
			}
		}
		try {
			this.conn.setAutoCommit(true);
		} catch (Exception exc) {
			MysqlAdapter.LOGGER.error("Failed setting autocommit", exc);
		}
		return success;
	}

	public long attemptReturnId(Callable<Long> callable) {
		long result = 0;
		try {
			if (!conn.isValid(0)) {
				MysqlAdapter.LOGGER.info("Invalid MySQL connection - reconnecting...");
				reconnect();
			}
		} catch (Exception exc) {
			MysqlAdapter.LOGGER.error("Failed reconnect", exc);
			return -1;
		}
		try {
			this.conn.setAutoCommit(false);
			result = callable.call();
			this.conn.commit();
		} catch (Exception exc) {
			MysqlAdapter.LOGGER.error("Failed attempt", exc);
			result = -1;
			this.reconnect();
		}
		if (result <= 0) {
			try {
				this.conn.setAutoCommit(false);
				result = callable.call();
				this.conn.commit();
			} catch (Exception exc) {
				MysqlAdapter.LOGGER.error("Failed retry", exc);
				result = -1;
			}
		}
		try {
			this.conn.setAutoCommit(true);
		} catch (Exception exc) {
			MysqlAdapter.LOGGER.error("Failed setting autocommit", exc);
		}
		return result;
	}
	
	public static int countCharacters(char ch, String str) {
		if (str==null || str.length()==0) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf((int)'?', idx)) > 0) {
			count++;
			idx++;
		}
		return count;
	}
}
