package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.product.Product;
import zpi.product.ProductDoesNotExistException;
import zpi.product.SimpleProductDAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class ComputeTaxController {
	
	public static Handler computeTax = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);

		Product product = null;
		try{
			product = DAOFactory.getIProductDAO().getProduct(ctx.queryParam("product"));
			var state = DAOFactory.getIUSStateDAO().getUSStateByName(ctx.queryParam("state"));
			double amount = Double.parseDouble(ctx.queryParam("amount"));
			double base_price = Double.parseDouble(ctx.queryParam("base_price"));
			double expected_price = Double.parseDouble(ctx.queryParam("expected_price"));
			
			product.setBasePrice(base_price);
			product.setExpectedPrice(expected_price);
			if (state.isPresent()) {
				model.put("profitForOnePiece", String.valueOf(state.get().computeProfit(product)));
				model.put("profitForAmount", String.valueOf(state.get().computeProfit(product) * amount));

				ctx.render(Paths.Template.SINGLE_RESULT, model);
			} else {
				ctx.html("Such state does not exist!");
			}
		}catch (ProductDoesNotExistException e){
			ctx.html("Such product does not exist!");
		}
		
	};
}
