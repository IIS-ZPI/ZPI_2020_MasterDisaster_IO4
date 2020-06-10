package zpi;

import io.javalin.Javalin;
import zpi.controllers.AboutPaigeController;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.ErrorPageController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.db.Database;
import zpi.product.MSSQLProductDAO;
import zpi.product.ProductController;
import zpi.product.SimpleProductDAO;
import zpi.state.MSSQUSStateDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

import java.net.URISyntaxException;
import java.net.URL;

public class Main {
	
	public static void main(String[] args) {
		Database database = new Database();
		if(args.length > 0)
			database.createDBConnectionFromCommandLine(args);
		else
			database.createDBConnection("HOSTNAME", "DBNAME", "DBUSER", "DBPASSWORD");
//
//		database.dropAll();
//		database.initializeTables();
//		database.fillStatesAndTaxesTablesFromCSV("/additional/statesTaxes.csv");
//		database.addBaseExceptionForThreeStates();

//		DAOFactory.registerUSStateDao(new SimpleUSStateDAO());
//		DAOFactory.registerProductDao(new SimpleProductDAO());
		DAOFactory.registerUSStateDao(new MSSQUSStateDAO(database));
		DAOFactory.registerProductDao(new MSSQLProductDAO(database));
		
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public");
		}).start(getHerokuAssignedPort());
		
		
		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);
		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.put(Paths.Web.ALL_STATES, USStateController.editStateTaxesPut);
		app.get(Paths.Web.ALL_PRODUCTS, ProductController.allProductsDisplay);
		app.get(Paths.Web.ABOUT, AboutPaigeController.aboutPage);
		app.post(Paths.Web.ALL_PRODUCTS, ProductController.addProductPost);
		app.put(Paths.Web.ALL_PRODUCTS, ProductController.editProductPut);
		app.delete(Paths.Web.ALL_PRODUCTS, ProductController.removeProduct);
		app.error(404, ErrorPageController.error404);
	}
	
	private static int getHerokuAssignedPort() {
		String herokuPort = System.getenv("PORT");
		if (herokuPort != null) {
			return Integer.parseInt(herokuPort);
		}
		return 7000;
	}
}
