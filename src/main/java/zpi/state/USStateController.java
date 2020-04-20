package zpi.state;

import io.javalin.http.Handler;
import zpi.category.Category;
import zpi.category.CategoryTransalator;
import zpi.utils.Paths;
import zpi.utils.RequestUtil;
import zpi.utils.ViewUtil;

import java.util.Map;

public class USStateController {
	public static Handler allStatesDisplay = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		//daos
		USStateDAO usStateDAO = USStateDAO.getInstance();
		model.put("title", "CTC: All states");
		
		model.put("categoriesTranslator", new CategoryTransalator());
		model.put("categories", Category.values());
		model.put("states", usStateDAO.getStates());
		
		//render
		ctx.render(Paths.Template.ALL_STATES, model);
	};
	
	public static Handler singleStateDisplay = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		var state = USStateDAO.getInstance().getUSStateByName(RequestUtil.getStateName(ctx));
		if (state.isPresent()) {
			model.put("title", "CTC: " + state.get() + " state");
			model.put("state", state.get());
			
			model.put("categories", Category.values());
			model.put("categoriesTranslator", new CategoryTransalator());
			
			ctx.render(Paths.Template.SINGLE_STATE, model);
		} else {
			ctx.html("Not found");
		}
	};
}
