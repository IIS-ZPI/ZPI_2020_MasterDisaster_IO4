import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.product.ProductController;
import zpi.product.SimpleProductDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

class HTTPRequestFactory {
	static final int PORT = 1234;
	static final String URL_BASE = "http://localhost:1234";
	static final String SIMPLE_TAX_URL = Paths.Web.SIMPLE_TAX;
	static final String ALL_PRODUCTS_URL = Paths.Web.ALL_PRODUCTS;
	static final String ALL_STATES_URL = Paths.Web.ALL_STATES;

	static final String NOT_FOUND_MESSAGE = "Not found";

	static final int OK_STATUS = 200;
	static final int NOT_FOUND_STATUS = 404;
	static final int BAD_REQUEST_STATUS = 400;
	static final int CONFLICT_STATUS = 409;


	static Javalin createApp(){
		DAOFactory.registerUSStateDao(new SimpleUSStateDAO());
		DAOFactory.registerProductDao(new SimpleProductDAO());

		io.javalin.Javalin app = io.javalin.Javalin.create(config -> {
			config.addStaticFiles("/public");
		});

		app.get(Paths.Web.INDEX, MainPageController.mainPage);
		app.get(Paths.Web.SIMPLE_TAX, ComputeTaxController.computeTax);

		app.get(Paths.Web.ALL_STATES, USStateController.allStatesDisplay);
		app.put(Paths.Web.ALL_STATES, USStateController.editStateTaxesPut);

		app.get(Paths.Web.ALL_PRODUCTS, ProductController.allProductsDisplay);
		app.post(Paths.Web.ALL_PRODUCTS, ProductController.addProductPost);
		app.put(Paths.Web.ALL_PRODUCTS, ProductController.editProductPut);
		app.delete(Paths.Web.ALL_PRODUCTS, ProductController.removeProduct);
		return app;
	}

	static void setDefaultBaseUrl(){
			Unirest.config().defaultBaseUrl(HTTPRequestFactory.URL_BASE);
	}

	static HttpResponse getResponse(String url){
		return Unirest.get(url).asString();
	}

	static HttpResponse postResponse(String url, JSONArray body){
		return Unirest.post(url).body(body).asString();
	}

	static HttpResponse putResponse(String url, JSONArray body){
		return Unirest.put(url).body(body).asString();
	}

	static HttpResponse deleteResponse(String url, JSONArray body){
		return Unirest.delete(url).body(body).asString();
	}
}
