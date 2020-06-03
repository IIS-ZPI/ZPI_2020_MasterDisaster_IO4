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

	enum Type {POST, PUT, DELETE}
	@Parameterized.Parameters
	public static Collection values() {
		return Arrays.asList(new Object[][] {
				{ Type.PUT, HTTPRequestFactory.OK_STATUS, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, "2.0" },
				{ Type.PUT, HTTPRequestFactory.BAD_REQUEST_STATUS, NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE },
				{ Type.PUT, HTTPRequestFactory.BAD_REQUEST_STATUS, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE },
				{ Type.POST, HTTPRequestFactory.OK_STATUS, NON_EXISTING_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE },
				{ Type.POST, HTTPRequestFactory.CONFLICT_STATUS, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, IRRELEVANT_BASE_PRICE },
				{ Type.POST, HTTPRequestFactory.BAD_REQUEST_STATUS, IRRELEVANT_PRODUCT_NAME, IRRELEVANT_PRODUCT_CATEGORY, WRONG_BASE_PRICE },
				{ Type.DELETE, HTTPRequestFactory.OK_STATUS, "Milk", "", "" },
				{ Type.DELETE, HTTPRequestFactory.BAD_REQUEST_STATUS, "TestProductName2", "", "" },
		});
	}

	@Parameterized.Parameter public Type type;
	@Parameterized.Parameter(1) public int status;
	@Parameterized.Parameter(2) public String productName;
	@Parameterized.Parameter(3) public String productCategory;
	@Parameterized.Parameter(4) public String basePrice;

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
		assertThat(response.getStatus()).isEqualTo(status);

//		String body = (String)response.getBody();
//		if(status == HTTPRequestFactory.OK_STATUS){
//			assertThat(body).contains(productName);
//			assertThat(body).contains(productCategory);
//			assertThat(body).contains(basePrice);
//		}
	}

//	@Test
//	public void POST_toCheckIfNoProductDataEntered() {
//		HttpResponse response = HTTPRequestFactory.postResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, new JSONArray());
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST_STATUS);
//	}

	@Test
	public void DELETE_toCheckIfDeletedProduct() {
		Assume.assumeTrue(type == Type.DELETE);
		var arr = generateBody(productName, productCategory, basePrice);

		HttpResponse response = HTTPRequestFactory.deleteResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		assertThat(response.getStatus()).isEqualTo(status);

		String body = (String)response.getBody();
		assertThat(body).doesNotContain(productName);
	}

	@Test
	public void PUT_toCheckIfChangedProduct() {
		Assume.assumeTrue(type == Type.PUT);
		var arr = generateBody( productName, productCategory, basePrice);

		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_PRODUCTS_URL, arr);
		assertThat(response.getStatus()).isEqualTo(status);

//		String body = (String)response.getBody();
//		if(status == HTTPRequestFactory.OK_STATUS){
//			assertThat(body).contains(productName);
//			assertThat(body).contains(productCategory);
//			assertThat(body).contains(basePrice);
//		}
	}
}
