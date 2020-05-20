import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.*;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HTTPRequestProductTest {
	private static Javalin app = null;
	private final String IRRELEVANT_PRODUCT_NAME = "Tomato";
	private final String IRRELEVANT_PRODUCT_NAME2 = "Milk";
	private final String IRRELEVANT_PRODUCT_CATEGORY = "GROCERIES";
	private final String IRRELEVANT_BASE_PRICE = "1.0";
	private final String NON_EXISTING_PRODUCT_NAME = "TestProductName";
	private final String NON_EXISTING_PRODUCT_NAME2 = "TestProductName2";
	private final String WRONG_BASE_PRICE = "TestBasePrice";

	@BeforeClass
	public static void init() {
		HTTPRequestFactory.setDefaultBaseUrl();
		app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
	}

	@AfterClass
	public static void destruct(){
		app.stop();
	}

	private JSONArray generateBody(String productName, String categoryName, String basePrice) {
		var arr = new JSONArray();
		arr.put(new JSONObject(Map.of("name", "productName", "value", productName)));
		arr.put(new JSONObject(Map.of("name", "categoryName", "value", categoryName)));
		arr.put(new JSONObject(Map.of("name", "basePrice", "value", basePrice)));
		return arr;
	}


	@Test
	public void GET_toCheckAllProductsStatus() {
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.ALL_PRODUCTS_URL);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
	}

	@Test
	public void POST_toCheckIfAddedNewProduct() {
		var arr = generateBody(NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);

		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains(NON_EXISTING_PRODUCT_NAME);
		assertThat(body).contains(IRRELEVANT_PRODUCT_CATEGORY);
		assertThat(body).contains(IRRELEVANT_BASE_PRICE);
	}

	@Test
	public void POST_toCheckIfAddedExistingProduct() {
		var arr = generateBody(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);

		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains("Product with such name exists");
	}

	@Test
	public void POST_toCheckIfNoProductDataEntered() {
		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, new JSONArray());
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST);
	}

	@Test
	public void POST_toCheckIfBasePriceIsNotANumber() {
		var arr = generateBody(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE);

		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.WRONG_DATA_MESSAGE);
	}

	@Test
	public void DELETE_toCheckExistingProduct() {
		var arr = generateBody(IRRELEVANT_PRODUCT_NAME2, "", "");

		HttpResponse response = HTTPRequestFactory.deleteResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).doesNotContain(IRRELEVANT_PRODUCT_NAME2);
	}

	@Test
	public void DELETE_toCheckNonExistingProduct() {
		var arr = generateBody(NON_EXISTING_PRODUCT_NAME2, "", "");

		HttpResponse response = HTTPRequestFactory.deleteResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
	}

	@Test
	public void PUT_toCheckExistingProduct() {
		String newBasePrice = "2.0";
		var arr = generateBody(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, newBasePrice);

		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains(IRRELEVANT_PRODUCT_NAME);
		assertThat(body).contains(IRRELEVANT_PRODUCT_CATEGORY);
		assertThat(body).contains(newBasePrice);
	}

	@Test
	public void PUT_toCheckNonExistingProduct() {
		var arr = generateBody(NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE);

		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
	}

	@Test
	public void PUT_toCheckIfBasePriceIsNotNumber() {
		var arr = generateBody(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE);

		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.WRONG_DATA_MESSAGE);
	}
}
