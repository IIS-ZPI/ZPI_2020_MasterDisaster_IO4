package zpi.product;

import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import zpi.category.Category;
import zpi.category.CategoryTransalator;
import zpi.dao.DAOFactory;
import zpi.state.IUSStateDAO;
import zpi.utils.Paths;
import zpi.utils.RequestUtil;
import zpi.utils.ViewUtil;

import java.util.Map;

public class ProductController {
	public static Handler allProductsDisplay = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		//daos
		IProductDAO dao = DAOFactory.getIProductDAO();
		model.put("title", "CTC: All products");
		model.put("categoriesTranslator", new CategoryTransalator());
		model.put("categories", Category.values());
		model.put("products", dao.getProducts());
		
		ctx.render(Paths.Template.ALL_PRODUCTS, model);
	};
	
	
	public static Handler editProductPut = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		var paramsMap = RequestUtil.parseJSONStandardArrayToMap(ctx);
		if (RequestUtil.isMapContainsRequiredParams(paramsMap, "productName", "categoryName", "basePrice")) {
			
			try {
				IProductDAO dao = DAOFactory.getIProductDAO();
				dao.updateProductBasePrice(paramsMap.get("productName"), Double.parseDouble(paramsMap.get("basePrice")));
				dao.updateProductCategory(paramsMap.get("productName"), Category.valueOf(paramsMap.get("categoryName")));
				
				model.put("title", "CTC: All products");
				model.put("categoriesTranslator", new CategoryTransalator());
				model.put("categories", Category.values());
				model.put("products", dao.getProducts());
				
				
				ctx.render(Paths.Template.ALL_PRODUCTS, model);
				
			} catch (ProductDoesNotExistException | NumberFormatException e) {
				ctx.status(HttpStatus.BAD_REQUEST_400);
			}
		} else {
			ctx.status(HttpStatus.BAD_REQUEST_400);
		}
		
	};
	
	public static Handler removeProduct = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		var paramsMap = RequestUtil.parseJSONStandardArrayToMap(ctx);
		if (RequestUtil.isMapContainsRequiredParams(paramsMap, "productName")) {
			try {
				IProductDAO dao = DAOFactory.getIProductDAO();
				dao.removeProduct(paramsMap.get("productName"));
				
				model.put("title", "CTC: All products");
				model.put("categoriesTranslator", new CategoryTransalator());
				model.put("categories", Category.values());
				model.put("products", dao.getProducts());
				
				ctx.render(Paths.Template.ALL_PRODUCTS, model);
			} catch (ProductDoesNotExistException e) {
				ctx.status(HttpStatus.BAD_REQUEST_400);
			}
		} else {
			ctx.status(HttpStatus.BAD_REQUEST_400);
		}
	};
	
	public static Handler addProductPost = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		
		var paramsMap = RequestUtil.parseJSONStandardArrayToMap(ctx);
		if (RequestUtil.isMapContainsRequiredParams(paramsMap, "productName", "categoryName", "basePrice")) {
			try {
				String productName = paramsMap.get("productName");
				Category category = Category.valueOf(paramsMap.get("categoryName"));
				double basePrice = Double.parseDouble(paramsMap.get("basePrice"));
				
				IProductDAO dao = DAOFactory.getIProductDAO();
				dao.addProduct(productName, category, basePrice);
				
				model.put("title", "CTC: All products");
				model.put("categoriesTranslator", new CategoryTransalator());
				model.put("categories", Category.values());
				model.put("products", dao.getProducts());
				
				ctx.render(Paths.Template.ALL_PRODUCTS, model);
			} catch (ProductDuplicateException e) {
				ctx.status(HttpStatus.CONFLICT_409);
			} catch (NumberFormatException e) {
				ctx.status(HttpStatus.BAD_REQUEST_400);
			}
		} else {
			ctx.status(HttpStatus.BAD_REQUEST_400);
		}
	};
}
