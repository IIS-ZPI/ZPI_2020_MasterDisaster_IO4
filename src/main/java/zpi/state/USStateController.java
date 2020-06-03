package zpi.state;

import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJson;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import zpi.category.Category;
import zpi.category.CategoryTransalator;
import zpi.dao.DAOFactory;
import zpi.utils.Paths;
import zpi.utils.RequestUtil;
import zpi.utils.ViewUtil;

import java.util.Map;

public class USStateController {
	public static Handler allStatesDisplay = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		//daos
		IUSStateDAO usStateDAO = DAOFactory.getIUSStateDAO();
		model.put("title", "CTC: All states");
		
		model.put("categoriesTranslator", new CategoryTransalator());
		model.put("categories", Category.values());
		model.put("states", usStateDAO.getStates());
		
		//render
		ctx.render(Paths.Template.ALL_STATES, model);
	};
	
	public static Handler singleStateDisplay = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		var state = DAOFactory.getIUSStateDAO().getUSStateByName(RequestUtil.getStateName(ctx));
		if (state.isPresent()) {
			model.put("title", "CTC: " + state.get() + " state");
			model.put("state", state.get());
			
			model.put("categories", Category.values());
			model.put("categoriesTranslator", new CategoryTransalator());
			
			ctx.render(Paths.Template.SINGLE_STATE, model);
		} else {
			ctx.status(HttpStatus.BAD_REQUEST_400);
		}
	};
	
	public static Handler editStateTaxesPut = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		var stateDao = DAOFactory.getIUSStateDAO();
		var optState = stateDao.getUSStateByName(RequestUtil.getStateName(ctx));
		if (optState.isPresent()) {
			var state = optState.get();
			var jsonRequestBody = new JSONArray(ctx.body());
			
			try {
				for (int i = 0; i < jsonRequestBody.length(); ++i) {
					var jsonObject = jsonRequestBody.getJSONObject(i);
					var categoryForm = jsonObject.getDouble("value");
					var category = Category.valueOf(jsonObject.getString("name"));
					
					stateDao.editCategoryTax(state, category, categoryForm);
				}
				model.put("edit_failed", false);
				
			} catch (NumberFormatException | JSONException e) {
				//we can input here some additional information about ex. in which category there was an invalid value
				model.put("edit_failed", true);
			}
			
			model.put("title", "CTC: " + state + " state");
			model.put("state", state);
			
			model.put("categories", Category.values());
			model.put("categoriesTranslator", new CategoryTransalator());
			
			ctx.status(HttpStatus.OK_200);
		} else {
			ctx.status(HttpStatus.BAD_REQUEST_400);
		}
	};

}
