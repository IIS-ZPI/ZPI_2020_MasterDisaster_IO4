package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.product.ProductDAO;
import zpi.state.SimpleUSStateDAO;

public class ComputeTaxController {
	
	public static Handler computeTax = ctx -> {
		var product = ProductDAO.getInstance().getProductByName(ctx.queryParam("product"));
		var state = DAOFactory.getIUSStateDAO().getUSStateByName(ctx.queryParam("state"));
		double amount = Double.parseDouble(ctx.queryParam("amount"));
		double base_price = Double.parseDouble(ctx.queryParam("base_price"));
		double expected_price = Double.parseDouble(ctx.queryParam("expected_price"));
		
		product.get().setBasePrice(base_price);
		product.get().setExpectedPrice(expected_price);
		String output = "";
		if (product.isPresent() && state.isPresent()) {
			output += "Profit for 1 piece of the product: ";
			output += String.valueOf(state.get().computeProfit(product.get()));
			output += "$\n";
			output += "Profit for given amount of the product: ";
			output += String.valueOf(state.get().computeProfit(product.get()) * amount);
			output += "$\n";
		} else {
			output = "Such product or state does not exist!";
		}
		ctx.html(output);
	};
}
