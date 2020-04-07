package zpi.controllers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import zpi.daos.DAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.nio.file.Path;
import java.util.Map;

public class MainPageController {
	public static Handler mainPage = ctx -> {
		DAO dao = DAO.getInstance();
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		model.put("products", dao.getProducts());
		model.put("states", dao.getStates());

		ctx.render(Paths.Template.INDEX, model);
	};
}
