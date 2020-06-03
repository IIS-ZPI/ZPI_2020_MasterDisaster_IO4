package zpi;

import io.javalin.Javalin;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.ErrorPageController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.db.Database;
import zpi.product.MSSQLProductDAO;
import zpi.product.ProductController;
import zpi.product.SimpleProductDAO;
import zpi.state.MSSqlUSStateDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

public class Main {
	
	public static void main(String[] args) {
		Database database = new Database();
		if(args.length > 0)
			database.createDBConnectionFromCommandLine(args);
		else
			database.createDBConnection("HOSTNAME", "DBNAME", "DBUSER", "DBPASSWORD");
		
		database.initializeTables();
		
		DAOFactory.registerUSStateDao(new SimpleUSStateDAO());
		DAOFactory.registerProductDao(new MSSQLProductDAO(database));
		
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public");
		}).start(getHerokuAssignedPort());
		
		
		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);
		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.get(Paths.Web.SINGLE_STATE, USStateController.singleStateDisplay);
		app.get(Paths.Web.ALL_PRODUCTS, ProductController.allProductsDisplay);
		app.post(Paths.Web.ALL_PRODUCTS, ProductController.addProductPost);
		app.put(Paths.Web.ALL_PRODUCTS, ProductController.editProductPut);
		app.delete(Paths.Web.ALL_PRODUCTS, ProductController.removeProduct);
		app.put(Paths.Web.SINGLE_STATE, USStateController.editStateTaxesPut);
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
