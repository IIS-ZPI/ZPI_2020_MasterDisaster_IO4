package zpi.state;

import io.javalin.http.Handler;
import zpi.category.Category;
import zpi.category.CategoryTransalator;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class USStateController {
	public static Handler allStatesDisplay = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		//daos
		USStateDAO usStateDAO = USStateDAO.getInstance();
		
		model.put("categoriesTranslator", new CategoryTransalator());
		model.put("categories", Category.values());
		model.put("states", usStateDAO.getStates());
		
		//render
		ctx.render(Paths.Template.ALL_STATES, model);
	};
}
