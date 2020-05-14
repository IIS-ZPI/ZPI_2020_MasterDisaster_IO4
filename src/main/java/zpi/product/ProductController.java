package zpi.product;

import io.javalin.http.Handler;
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
		
		try {
			var category = RequestUtil.getProductCategory(ctx);
			var basePrice = RequestUtil.getProductBasePrice(ctx);
			IProductDAO dao = DAOFactory.getIProductDAO();
			dao.updateProductBasePrice(RequestUtil.getProductName(ctx), basePrice);
			dao.updateProductCategory(RequestUtil.getProductName(ctx), Category.valueOf(category));
			
			model.put("title", "CTC: All products");
			model.put("categoriesTranslator", new CategoryTransalator());
			model.put("categories", Category.values());
			model.put("products", dao.getProducts());
			
			
			ctx.render(Paths.Template.ALL_PRODUCTS, model);
			
		} catch (ProductDoesNotExistException e) {
			ctx.html("Not found");
		} catch (NumberFormatException e) {
			ctx.html("Wrong data");
		}
		
	};
	
	public static Handler removeProduct = ctx -> {
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		try {
			IProductDAO dao = DAOFactory.getIProductDAO();
			dao.removeProduct(RequestUtil.getProductName(ctx));
			
			model.put("title", "CTC: All products");
			model.put("categoriesTranslator", new CategoryTransalator());
			model.put("categories", Category.values());
			model.put("products", dao.getProducts());
			
			ctx.render(Paths.Template.ALL_PRODUCTS, model);
		} catch (ProductDoesNotExistException e) {
			ctx.html("Not found");
		}
	};
}
