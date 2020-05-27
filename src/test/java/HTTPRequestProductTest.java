import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(Parameterized.class)
public class HTTPRequestProductTest {
	private static Javalin app = null;
	private static final String IRRELEVANT_PRODUCT_NAME = "Tomato";
	private static final String IRRELEVANT_PRODUCT_CATEGORY = "GROCERIES";
	private static final String IRRELEVANT_BASE_PRICE = "1.0";
	private static final String NON_EXISTING_PRODUCT_NAME = "TestProductName";
	private static final String WRONG_BASE_PRICE = "TestBasePrice";

	private static final String SUCCESS_MESSAGE = "";
	private static final String FAIL_MESSAGE = "Product with such name exists";

	enum Type {POST, PUT, DELETE}
	@Parameterized.Parameters
	public static Collection values() {
		return Arrays.asList(new Object[][] {
				{ Type.PUT, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, "2.0", SUCCESS_MESSAGE},
				{ Type.PUT, NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE, HTTPRequestFactory.NOT_FOUND_MESSAGE},
				{ Type.PUT, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE, HTTPRequestFactory.WRONG_DATA_MESSAGE},
				{ Type.POST, NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE, SUCCESS_MESSAGE },
				{ Type.POST, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE, FAIL_MESSAGE },
				{ Type.POST, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE, HTTPRequestFactory.WRONG_DATA_MESSAGE },
				{ Type.DELETE, "Milk", "", "", SUCCESS_MESSAGE},
				{ Type.DELETE, "TestProductName2", "", "", HTTPRequestFactory.NOT_FOUND_MESSAGE},
		});
	}

	@Parameterized.Parameter public Type type;
	@Parameterized.Parameter(1) public String productName;
	@Parameterized.Parameter(2) public String productCategory;
	@Parameterized.Parameter(3) public String basePrice;
	@Parameterized.Parameter(4) public String returnMessage;

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
	public void POST_toCheckIfAddedProduct() {
		Assume.assumeTrue(type == Type.POST);
		var arr = generateBody(productName, productCategory, basePrice);

		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains(returnMessage);

		if(returnMessage.equals(SUCCESS_MESSAGE)){
			assertThat(body).contains(productName);
			assertThat(body).contains(productCategory);
			assertThat(body).contains(basePrice);
		}
	}

	@Test
	public void POST_toCheckIfNoProductDataEntered() {
		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, new JSONArray());
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST);
	}

	@Test
	public void DELETE_toCheckIfDeletedProduct() {
		Assume.assumeTrue(type == Type.DELETE);
		var arr = generateBody(productName, productCategory, basePrice);

		HttpResponse response = HTTPRequestFactory.deleteResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).doesNotContain(productName);
		assertThat(body).contains(returnMessage);
	}

	@Test
	public void PUT_toCheckIfChangedProduct() {
		Assume.assumeTrue(type == Type.PUT);
		var arr = generateBody( productName, productCategory, basePrice);

		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains(returnMessage);
		if(returnMessage.equals(SUCCESS_MESSAGE)){
			assertThat(body).contains(productName);
			assertThat(body).contains(productCategory);
			assertThat(body).contains(basePrice);
		}
	}
}
