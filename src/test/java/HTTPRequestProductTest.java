import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class HTTPRequestProductTest {
	private final String IRRELEVANT_PRODUCT_NAME = "Tomato";
	private final String IRRELEVANT_PRODUCT_CATEGORY = "GROCERIES";
	private final String IRRELEVANT_BASE_PRICE = "1.0";
	private final String NON_EXISTING_PRODUCT_NAME = "TestProductName";
	private final String WRONG_BASE_PRICE = "TestBasePrice";

	@Mock
	private Context ctx;

	private void generateContextForProductController(String productName, String categoryName, String basePrice){
		when(ctx.formParam("productName")).thenReturn(productName);
		when(ctx.formParam("categoryName")).thenReturn(categoryName);
		when(ctx.formParam("basePrice")).thenReturn(basePrice);
	}

	@Test
	public void GET_toCheckAllProductsStatus() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.ALL_PRODUCTS_URL);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		app.stop();
	}

	@Test
	public void POST_toCheckIfAddedNewProduct() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("");
		app.stop();
	}

	@Test
	public void POST_toCheckIfAddedExistingProduct() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("Product with such name exists");
		app.stop();
	}

	@Test
	public void POST_toCheckIfNoProductDataEntered() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = Unirest.post(HTTPRequestFactory.ALL_PRODUCTS_URL).asString();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("");
		app.stop();
	}

	@Test
	public void POST_toCheckIfBasePriceIsNotANumber() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.WRONG_DATA_MESSAGE);
		app.stop();
	}

	@Test
	public void DELETE_toCheckExistingProduct() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.deleteResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("");
		app.stop();
	}

	@Test
	public void DELETE_toCheckNonExistingProduct() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.deleteResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	@Test
	public void PUT_toCheckExistingProduct() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, "2.0");
		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("");
		app.stop();
	}

	@Test
	public void PUT_toCheckNonExistingProduct() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	@Test
	public void PUT_toCheckIfBasePriceIsNotNumber() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		generateContextForProductController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE);
		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, ctx);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.WRONG_DATA_MESSAGE);
		app.stop();
	}
}
