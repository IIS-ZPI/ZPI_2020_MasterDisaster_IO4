package zpi.db;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Database {
	private Connection connection;
	
	
	// environmental variables names set on Heroku
	// hostNameEnv = "HOSTNAME", dbNameEnv = "DBNAME", userEnv = "DBUSER", passwordEnv = "DBPASSWORD"
	public void createDBConnection(String hostNameEnv, String dbNameEnv, String userEnv, String passwordEnv) {
		// get credentials from env variables
		
		String hostName = System.getenv(hostNameEnv),
				dbName = System.getenv(dbNameEnv),
				user = System.getenv(userEnv),
				password = System.getenv(passwordEnv);
		try {
			if (hostName == null || dbName == null || user == null || password == null) {
				throw new SQLException("Not all Credentials are set: \n" +
						hostNameEnv + ": " + hostName + "\n" +
						dbNameEnv + ": " + dbName + "\n" +
						userEnv + ": " + user + "\n" +
						passwordEnv + ": " + password);
			}
			
			// Connect to database
			String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
					+ "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
			connection = DriverManager.getConnection(url);
		} catch (SQLException exc) {
			System.err.println("Problem with connecting to SQL server");
			exc.printStackTrace();
		}
	}
	
	public void dropAll() {
		final String sql = "DECLARE @sql NVARCHAR(2000)\n" +
				"\n" +
				"WHILE(EXISTS(SELECT 1 from INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE='FOREIGN KEY'))\n" +
				"BEGIN\n" +
				"    SELECT TOP 1 @sql=('ALTER TABLE ' + TABLE_SCHEMA + '.[' + TABLE_NAME + '] DROP CONSTRAINT [' + CONSTRAINT_NAME + ']')\n" +
				"    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS\n" +
				"    WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'\n" +
				"    EXEC(@sql)\n" +
				"    PRINT @sql\n" +
				"END\n" +
				"\n" +
				"WHILE(EXISTS(SELECT * from INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME != '__MigrationHistory' AND TABLE_NAME != 'database_firewall_rules'))\n" +
				"BEGIN\n" +
				"    SELECT TOP 1 @sql=('DROP TABLE ' + TABLE_SCHEMA + '.[' + TABLE_NAME + ']')\n" +
				"    FROM INFORMATION_SCHEMA.TABLES\n" +
				"    WHERE TABLE_NAME != '__MigrationHistory' AND TABLE_NAME != 'database_firewall_rules'\n" +
				"    EXEC(@sql)\n" +
				"    PRINT @sql\n" +
				"END";
		
		try {
			connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void initializeTables() {
		try {
			createCategoriesTable();
			createStatesTable();
			createProductsTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createCategoriesTable() throws SQLException {
		final String sql =
				"CREATE TABLE Categories (\n" +
						"  id INT NOT NULL IDENTITY(1,1),\n" +
						"  category VARCHAR(200),\n" +
						"  PRIMARY KEY (id)\n" +
						");\n" +
						"\n" +
						"INSERT INTO Categories (category) VALUES\n" +
						"('GROCERIES'),\n" +
						"('PREPARED_FOOD'),\n" +
						"('PRESCRIPTION_DRUG'),\n" +
						"('NON_PRESCRIPTION_DRUG'),\n" +
						"('CLOTHING'),\n" +
						"('INTANGIBLES');\n";
		connection.createStatement().executeUpdate(sql);
	}
	
	private void createStatesTable() throws SQLException {
		final String sql = "CREATE TABLE States (\n" +
				"  id INT NOT NULL IDENTITY(1,1),\n" +
				"  stateName VARCHAR(100),\n" +
				"  PRIMARY KEY (id)\n" +
				");\n" +
				"\n" +
				"CREATE TABLE Taxes (\n" +
				"  stateID INT,\n" +
				"  categoryID INT,\n" +
				"  tax FLOAT,\n" +
				"  valueWithoutTax FLOAT,\n" +
				"  FOREIGN KEY (stateID) REFERENCES States(id),\n" +
				"  FOREIGN KEY (categoryID) REFERENCES Categories(id)\n" +
				");";
		connection.createStatement().executeUpdate(sql);
		
		
		final String insertStates = "INSERT INTO States (stateName) VALUES ('Alabama');\nINSERT INTO Taxes VALUES(1, 1, 2, 50);\n";
		connection.createStatement().executeUpdate(insertStates );
	}
	
	private void createProductsTable() throws SQLException {
		final String sql = "CREATE TABLE Products (\n" +
				"  productName VARCHAR(200),\n" +
				"  basePrice FLOAT,\n" +
				"  categoryID INT,\n" +
				"  PRIMARY KEY (productName),\n" +
				"  FOREIGN KEY (categoryID) REFERENCES Categories(id) ON DELETE CASCADE ON UPDATE CASCADE" +
				");\n";
		connection.createStatement().executeUpdate(sql);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}
	
	public void createDBConnectionFromCommandLine(String[] args) {
		var params =
				IntStream.range(0, args.length - 1)
						.filter(i -> i % 2 == 0)
						.boxed()
						.collect(Collectors.toMap(i -> args[i], i -> args[i + 1]));
		
		String hostName = params.get("--HOSTNAME"),
				dbName = params.get("--DBNAME"),
				user = params.get("--DBUSER"),
				password = params.get("--DBPASSWORD");
		try {
			// Connect to database
			String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
					+ "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
			connection = DriverManager.getConnection(url);
		} catch (SQLException exc) {
			System.err.println("Problem with connecting to SQL server");
			exc.printStackTrace();
		}
	}
	
	private static String SQLExample(Connection connection) {
		
		StringBuilder outputBuilder = new StringBuilder();
		try {
			String schema = connection.getSchema();
			System.out.println("Successful connection - Schema: " + schema);
			
			outputBuilder.append("Query data example:\n");
			outputBuilder.append("=========================================\n");
			
			// Create and execute a SELECT SQL statement.
			String selectSql = "SELECT TOP (1000) * FROM [dbo].[test_table]";
			
			try (Statement statement = connection.createStatement();
			     ResultSet resultSet = statement.executeQuery(selectSql)) {
				
				// Print results from select statement
				outputBuilder.append("Top 20 categories:\n");
				while (resultSet.next()) {
					outputBuilder.append(resultSet.getString(1));
					outputBuilder.append(" ");
					outputBuilder.append(resultSet.getString(2));
				}
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return outputBuilder.toString();
	}
	
}
