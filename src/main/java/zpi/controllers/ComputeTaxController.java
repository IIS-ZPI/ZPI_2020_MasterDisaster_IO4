package zpi.controllers;

import io.javalin.http.Handler;
import zpi.product.ProductDAO;
import zpi.state.USStateDAO;

public class ComputeTaxController {
	
	public static Handler computeTax = ctx -> {
		var product = ProductDAO.getInstance().getProductByName(ctx.queryParam("product"));
		var state = USStateDAO.getInstance().getUSStateByName(ctx.queryParam("state"));
		double amount = Double.parseDouble(ctx.queryParam("amount"));
		
		String output = "";
		if (product.isPresent() && state.isPresent()) {
			output += "Final price is: ";
			output += String.valueOf((state.get().computeFinalPriceOfProduct(product.get()) * amount));
			output += "$";
		} else {
			output = "Such product or state does not exist!";
		}
		ctx.html(output);
	};
}
