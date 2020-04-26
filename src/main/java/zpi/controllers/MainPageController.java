package zpi.controllers;

import io.javalin.http.Handler;
import zpi.state.USStateDAO;
import zpi.product.ProductDAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class MainPageController {
	public static Handler mainPage = ctx -> {
		USStateDAO stateDAO = USStateDAO.getInstance();
		ProductDAO productDao = ProductDAO.getInstance();

		Map<String, Object> model = ViewUtil.baseModel(ctx);
		model.put("products", productDao.getProducts());
		model.put("states", stateDAO.getStates());

		ctx.render(Paths.Template.INDEX, model);
	};
}
