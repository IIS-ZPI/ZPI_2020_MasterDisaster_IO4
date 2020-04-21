package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.state.IUSStateDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.product.ProductDAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class MainPageController {
	public static Handler mainPage = ctx -> {
		ProductDAO productDao = ProductDAO.getInstance();
		
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		model.put("title", "Compute tax calculator");
		model.put("products", productDao.getProducts());
		model.put("states", DAOFactory.getIUSStateDAO().getStates());

		ctx.render(Paths.Template.INDEX, model);
	};
}
