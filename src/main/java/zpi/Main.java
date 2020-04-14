package zpi;

import io.javalin.Javalin;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.MainPageController;
import zpi.state.USStateController;
import zpi.utils.Paths;

public class Main {
	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public");
		}).start(7777);
		
		
		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);
		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.get(Paths.Web.SINGLE_STATE, USStateController.singleStateDisplay);
		
	}
}
