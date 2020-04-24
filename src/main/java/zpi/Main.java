package zpi;

import io.javalin.Javalin;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.MainPageController;
import zpi.utils.Paths;

public class Main {
	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> {
		}).start(getHerokuAssignedPort());


		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);
	}

	private static int getHerokuAssignedPort() {
		String herokuPort = System.getenv("PORT");
		if (herokuPort != null) {
			return Integer.parseInt(herokuPort);
		}
		return 7000;
	}
}
