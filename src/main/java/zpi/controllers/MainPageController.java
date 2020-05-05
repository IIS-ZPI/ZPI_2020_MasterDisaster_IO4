package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.product.IProductDAO;
import zpi.product.SimpleProductDAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class MainPageController {
	public static Handler mainPage = ctx -> {
		IProductDAO simpleProductDao = DAOFactory.getIProductDAO();

		Map<String, Object> model = ViewUtil.baseModel(ctx);
		model.put("title", "Compute tax calculator");
		model.put("products", simpleProductDao.getProducts());
		model.put("states", DAOFactory.getIUSStateDAO().getStates());
		
		ctx.render(Paths.Template.INDEX, model);
	};
}
