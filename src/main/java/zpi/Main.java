package zpi;

import io.javalin.Javalin;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.ErrorPageController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.product.ProductController;
import zpi.product.SimpleProductDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

import java.sql.*;

public class Main {

	public static void main(String[] args) {

		DAOFactory.registerUSStateDao(new SimpleUSStateDAO());
		DAOFactory.registerProductDao(new SimpleProductDAO());
		
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public");
		}).start(getHerokuAssignedPort());


		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);
		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.get(Paths.Web.SINGLE_STATE, USStateController.singleStateDisplay);
		app.get(Paths.Web.ALL_PRODUCTS, ProductController.allProductsDisplay);
		app.post(Paths.Web.ALL_PRODUCTS, ProductController.editProductPost);
		app.post(Paths.Web.SINGLE_STATE, USStateController.editStateTaxesPost);
		app.error(404, ErrorPageController.error404);
	}
	
	private static int getHerokuAssignedPort() {
		String herokuPort = System.getenv("PORT");
		if (herokuPort != null) {
			return Integer.parseInt(herokuPort);
		}
		return 7000;
	}

    // environmental variables names set on Heroku
    // hostNameEnv = "HOSTNAME", dbNameEnv = "DBNAME", userEnv = "DBUSER", passwordEnv = "DBPASSWORD"
    private static Connection CreateDBConnection(String hostNameEnv, String dbNameEnv, String userEnv, String passwordEnv) {
        // get credentials from env variables

        String hostName = System.getenv(hostNameEnv),
                dbName = System.getenv(dbNameEnv),
                user = System.getenv(userEnv),
                password = System.getenv(passwordEnv);
        Connection connection = null;
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
            System.out.println(exc);
        }

        return connection;
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
