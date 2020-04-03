package zpi.sales;

import io.javalin.Javalin;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Core {
	static Map<String, String> reservations = new HashMap<String, String>() ;
	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public/index.html");
		}).start(7777);
		
		app.post("/choose-state", ctx -> {
			reservations.put(ctx.formParam("state"), ctx.formParam("category"));
			ctx.html("You've choosen the state");
		});

		app.get("/check-taxes", ctx -> {
			ctx.html(reservations.get(ctx.queryParam("state")));
		});
	}
}
