import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.product.ProductController;
import zpi.product.SimpleProductDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

public class HTTPRequestFactory {
	public static final int PORT = 1234;
	public static final String URL_BASE = "http://localhost:1234";
	public static final String SIMPLE_TAX_URL = URL_BASE + Paths.Web.SIMPLE_TAX;
	public static final String ALL_PRODUCTS_URL = URL_BASE + Paths.Web.ALL_PRODUCTS;
	public static final String ALL_STATES_URL = URL_BASE + Paths.Web.ALL_STATES;
	public static final String SINGLE_STATE_URL = URL_BASE + Paths.Web.SINGLE_STATE;

	public static final String NOT_FOUND_MESSAGE = "Not found";
	public static final String NON_EXISTING_PRODUCT_MESSAGE = "Such product does not exist!";
	public static final String WRONG_DATA_MESSAGE = "Wrong data";

	public static final int OK_STATUS = 200;
	public static final int NOT_FOUND_STATUS = 404;

	public static Javalin createApp(){
		DAOFactory.registerUSStateDao(new SimpleUSStateDAO());
		DAOFactory.registerProductDao(new SimpleProductDAO());

		io.javalin.Javalin app = io.javalin.Javalin.create(config -> {
			config.addStaticFiles("/public");
		});

		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);

		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.get(Paths.Web.SINGLE_STATE, USStateController.singleStateDisplay);
		app.get(Paths.Web.ALL_PRODUCTS, ProductController.allProductsDisplay);

		app.post(Paths.Web.ALL_PRODUCTS, ProductController.addProductPost);
		app.put(Paths.Web.ALL_PRODUCTS, ProductController.editProductPut);
		app.delete(Paths.Web.ALL_PRODUCTS, ProductController.removeProduct);
		app.put(Paths.Web.SINGLE_STATE, USStateController.editStateTaxesPut);
		return app;
	}

	public static HttpResponse getResponse(String url){
		return Unirest.get(url).asString();
	}

	public static HttpResponse postResponse(String url, Object body){
		return Unirest.post(url).body(body).asString();
	}

	public static HttpResponse putResponse(String url, Object body){
		return Unirest.delete(url).body(body).asString();
	}

	public static HttpResponse deleteResponse(String url, Object body){
		return Unirest.delete(url).body(body).asString();
	}

}
