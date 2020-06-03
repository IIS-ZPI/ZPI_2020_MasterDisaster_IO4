//package java.zpi.sales;

import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class HTTPRequestComputeTaxTest {
	private static Javalin app = null;

	private final String IRRELEVANT_AMOUNT = "2";
	private final String IRRELEVANT_BASE_PRICE = "1.0";
	private final String IRRELEVANT_EXPECTED_PRICE = "3.0";

	private final String IRRELEVANT_PRODUCT_NAME = "Tomato";
	private final String IRRELEVANT_STATE_NAME = "Alaska";

	@Mock
	private Context ctx;

	@BeforeClass
	public static void init(){
		HTTPRequestFactory.setDefaultBaseUrl();
		app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
	}

	@AfterClass
	public static void destruct(){
		app.stop();
	}

	// compute tax:
	//		state/product/both niewybrani
	//		amount of product = 0/ujemna/dodatnia/string/null
	//		base price = 0/ujemna/dodatnia/string/null
	//		expected price = 0/ujemna/dodatnia/string/null

	@Test
	public void GET_toCheckMainPageStatus() {
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.URL_BASE);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
	}

	@Test
	public void GET_toCheckNotExistingPageStatus() {
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.URL_BASE + "/notExistingPageTest");
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.NOT_FOUND_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
	}

	private void generateContextForComputeTaxController(String productName, String stateName, String amountOfProduct,
														String basePrice, String expectedPrice){
		when(ctx.queryParam("product")).thenReturn(productName);
		when(ctx.queryParam("state")).thenReturn(stateName);
		when(ctx.queryParam("amount")).thenReturn(amountOfProduct);
		when(ctx.queryParam("base_price")).thenReturn(basePrice);
		when(ctx.queryParam("expected_price")).thenReturn(expectedPrice);
	}

//	@Test
//	public void GET_toCheckStatusIfNoProductDataEntered() {
//		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.SIMPLE_TAX_URL);
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
//		assertThat(response.getBody()).isEqualTo(NON_EXISTING_PRODUCT_MESSAGE);
//	}
//
//	@Test
//	public void GET_toCheckStatusOfExistingProduct() throws Exception {
//		generateContextForComputeTaxController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_STATE_NAME, IRRELEVANT_AMOUNT, IRRELEVANT_BASE_PRICE, IRRELEVANT_EXPECTED_PRICE);
//		ComputeTaxController.computeTax.handle(ctx);
//		//verify(ctx).status(HTTPRequestFactory.OK_STATUS);
//		verify(ctx).html("Profit for 1 piece of the product: 2.0$\nProfit for given amount of the product: 4.0$\n");
//	}
}
