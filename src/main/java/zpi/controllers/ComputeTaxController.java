package zpi.controllers;

import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
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
			double expected_price = Double.parseDouble(ctx.queryParam("expected_price"));
			
			product.setExpectedPrice(expected_price);
			if (state.isPresent()) {
				model.put("title", "CTC: Profit");
				model.put("profitForOnePiece", String.format("%.2f", (state.get().computeProfit(product, amount))));
				model.put("profitForAmount", String.format("%.2f", (state.get().computeProfit(product, amount) * amount)));
				model.put("product", product.getName());
				model.put("state", state.get().getName());

				ctx.render(Paths.Template.SINGLE_RESULT, model);
				ctx.status(HttpStatus.OK_200);
			} else {
				ctx.status(HttpStatus.BAD_REQUEST_400);
			}
		}catch (ProductDoesNotExistException e){
			ctx.status(HttpStatus.BAD_REQUEST_400);
		}
	};
}
