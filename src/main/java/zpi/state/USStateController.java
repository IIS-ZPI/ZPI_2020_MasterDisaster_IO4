package zpi.state;

import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
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
		model.put("mathTools", Math.class);
		
		model.put("categoriesTranslator", new CategoryTransalator());
		model.put("categories", Category.values());
		model.put("states", usStateDAO.getStates());
		
		//render
		ctx.render(Paths.Template.ALL_STATES, model);
	};
	
	public static Handler editStateTaxesPut = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		var stateDao = DAOFactory.getIUSStateDAO();
		var paramsMap = RequestUtil.parseJSONStandardArrayToMap(ctx);
		var state = stateDao.getUSStateByName(paramsMap.get("stateName"));
		paramsMap.remove("stateName");
		if (state.isPresent()) {
			try{
				for (var pair : paramsMap.entrySet()) {
					if (pair.getKey().startsWith("valueWithoutTax"))
						stateDao.editCategoryValueWithoutTax(state.get(), Category.valueOf(pair.getKey().substring(15)), Double.valueOf(pair.getValue()));
					else
						stateDao.editCategoryBaseTax(state.get(), Category.valueOf(pair.getKey()), Double.parseDouble(pair.getValue())/100);
				}
				ctx.status(HttpStatus.OK_200);
			}catch(NumberFormatException e){
				ctx.status(HttpStatus.BAD_REQUEST_400);
			}
		} else {
			ctx.status(HttpStatus.NOT_FOUND_404);
		}
	};
	
}
