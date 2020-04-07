package zpi.controllers;

import io.javalin.http.Handler;
import zpi.product.ProductDao;

public class ComputeTaxController {
	
	public static Handler computeTax = ctx -> {
		var product = ProductDao.getInstance().getProductByName(ctx.queryParam("product"));
		String output = "";
		output = product.map(value -> value.getName() + value.getBasePrice()).orElse("Such product does not exist!");
		ctx.html(output);
	};
}
