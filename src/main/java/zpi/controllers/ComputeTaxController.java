package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.product.Product;
import zpi.product.ProductDoesNotExistException;
import zpi.product.SimpleProductDAO;

public class ComputeTaxController {
	
	public static Handler computeTax = ctx -> {
		Product product = null;
		String output = "";
		try{
			product = DAOFactory.getIProductDAO().getProduct(ctx.queryParam("product"));
			var state = DAOFactory.getIUSStateDAO().getUSStateByName(ctx.queryParam("state"));
			double amount = Double.parseDouble(ctx.queryParam("amount"));
			double base_price = Double.parseDouble(ctx.queryParam("base_price"));
			double expected_price = Double.parseDouble(ctx.queryParam("expected_price"));
			
			product.setBasePrice(base_price);
			product.setExpectedPrice(expected_price);
			if (state.isPresent()) {
				output += "Profit for 1 piece of the product: ";
				output += String.valueOf(state.get().computeProfit(product));
				output += "$\n";
				output += "Profit for given amount of the product: ";
				output += String.valueOf(state.get().computeProfit(product) * amount);
				output += "$\n";
			} else {
				output = "Such state does not exist!";
			}
		}catch (ProductDoesNotExistException e){
			output = "Such product does not exist!";
		}
		
		ctx.html(output);
	};
}
