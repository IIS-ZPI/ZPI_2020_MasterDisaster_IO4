package zpi.product;

import io.javalin.http.Handler;
import zpi.category.Category;
import zpi.category.CategoryTransalator;
import zpi.dao.DAOFactory;
import zpi.state.IUSStateDAO;
import zpi.utils.Paths;
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
}
