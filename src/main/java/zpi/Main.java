package zpi;

import io.javalin.Javalin;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

public class Main {

	public static void main(String[] args) {

		DAOFactory.registerUSStateDao(new SimpleUSStateDAO());
		
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public");
		}).start(getHerokuAssignedPort());


		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);
		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.get(Paths.Web.SINGLE_STATE, USStateController.singleStateDisplay);
		app.post(Paths.Web.SINGLE_STATE, USStateController.editStateTaxesPost);
	}
	
	private static int getHerokuAssignedPort() {
		String herokuPort = System.getenv("PORT");
		if (herokuPort != null) {
			return Integer.parseInt(herokuPort);
		}
		return 7000;
	}
}
