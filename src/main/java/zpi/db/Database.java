package zpi.db;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
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
	
	
	/**
	 * @param pathToCSV Path to CSV file which contains a states with their taxes for each category
	 */
	public void fillStatesAndTaxesTablesFromCSV(String pathToCSV) {
		final String insertIntoStates = "INSERT INTO States (stateName) VALUES ('%s');";
		final String insertIntoTaxes = "INSERT INTO Taxes VALUES ((SELECT id FROM States WHERE stateName='%s'), %d, %s, %f);";
		URL resource = getClass().getResource(pathToCSV);
		try {
			Files.lines(Paths.get(resource.toURI()))
					.forEach(l -> {
						var list = l.split(",");
						try {
							createStatement().executeUpdate(String.format(insertIntoStates, list[0]));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						IntStream.range(1, list.length)
								.forEach(index -> {
									try {
										createStatement().executeUpdate(String.format(insertIntoTaxes, list[0], index, list[index], 0.));
									} catch (SQLException e) {
										e.printStackTrace();
									}
								});
					});
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}
	
	public void addBaseExceptionForThreeStates() {
		final String updateTax = "UPDATE Taxes SET valueWithoutTax=%f WHERE stateID=(SELECT id FROM States WHERE stateName='%s') and categoryID=(SELECT id FROM Categories WHERE category='%s');";
		try {
			createStatement().executeUpdate(String.format(updateTax, 175., "Massachusetts", "Clothing"));
			createStatement().executeUpdate(String.format(updateTax, 110., "New York", "Clothing"));
			createStatement().executeUpdate(String.format(updateTax, 250., "Rhode Island", "Clothing"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
}
