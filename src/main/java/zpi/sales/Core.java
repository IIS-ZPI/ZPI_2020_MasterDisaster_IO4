package zpi.sales;

import io.javalin.Javalin;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Core {
	List<USState> states = new ArrayList<>();
	List<Product> products = new ArrayList<>();
	void prepareTempData() {
		Category groceries = new Category("Groceries");
		Category preparedFood = new Category("Prepared food");
		
		USState alabamaState = new USState("Alabama");
		
		alabamaState.addCategoryWithTax(groceries, Double.valueOf(4));
		alabamaState.addCategoryWithTax(preparedFood, Double.valueOf(5));
		
		states.add(alabamaState);
		
		products.add(new Product("Chocolate bar", 15, preparedFood));
		products.add(new Product("Milk", 10, preparedFood));
		products.add(new Product("Tomato", 3, groceries));
	}
	
	static Map<String, String> reservations = new HashMap<String, String>();
	
	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/public/index.html");
		}).start(7777);
		
		app.get("/compute-taxes", ctx -> {
			double amount = Double.parseDouble(ctx.queryParam("amount"));
			String state = ctx.queryParam("state");
			String category = ctx.queryParam("category");
			double taxRate = 0;
			switch (category) {
				case "Groceries":
					taxRate = 15;
					break;
				case "Clothing":
					taxRate = 10;
					break;
			}
			
			ctx.html(String.valueOf(amount + amount * taxRate));
		});
	}
}
