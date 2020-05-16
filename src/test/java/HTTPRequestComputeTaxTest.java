//package java.zpi.sales;

import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.http.protocol.HTTP;
import org.junit.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import zpi.controllers.ComputeTaxController;
import zpi.controllers.MainPageController;
import zpi.dao.DAOFactory;
import zpi.product.ProductController;
import zpi.product.SimpleProductDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USStateController;
import zpi.utils.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class HTTPRequestComputeTaxTest {
	private final String IRRELEVANT_AMOUNT = "2";
	private final String IRRELEVANT_BASE_PRICE = "1.0";
	private final String IRRELEVANT_EXPECTED_PRICE = "3.0";

	private final String IRRELEVANT_PRODUCT_NAME = "Tomato";
	private final String IRRELEVANT_STATE_NAME = "Alaska";

	@Mock
	private Context ctx;

	// compute tax:
	//		state/product/both niewybrani
	//		amount of product = 0/ujemna/dodatnia/string/null
	//		base price = 0/ujemna/dodatnia/string/null
	//		expected price = 0/ujemna/dodatnia/string/null

	@Test
	public void GET_toCheckMainPageStatus() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.URL_BASE);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		app.stop();
	}

	@Test
	public void GET_toCheckNotExistingPageStatus() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.URL_BASE + "/notExistingPageTest");
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.NOT_FOUND_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	private void generateContextForComputeTaxController(String productName, String stateName, String amountOfProduct, String basePrice, String expectedPrice){
		when(ctx.queryParam("product")).thenReturn(productName);
		when(ctx.queryParam("state")).thenReturn(stateName);
		when(ctx.queryParam("amount")).thenReturn(amountOfProduct);
		when(ctx.queryParam("base_price")).thenReturn(basePrice);
		when(ctx.queryParam("expected_price")).thenReturn(expectedPrice);
	}

//	@Test
//	public void GET_toCheckStatusIfNoProductDataEntered() {
//		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
//		HttpResponse response = HTTPRequestFactory.getResponse(SIMPLE_TAX_URL);
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
//		assertThat(response.getBody()).isEqualTo(NON_EXISTING_PRODUCT_MESSAGE);
//		app.stop();
//	}
//
//	@Test
//	public void GET_toCheckStatusOfExistingProduct() throws Exception {
//		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
//		generateContextForComputeTaxController(IRRELEVANT_PRODUCT_NAME, IRRELEVANT_STATE_NAME, IRRELEVANT_AMOUNT, IRRELEVANT_BASE_PRICE, IRRELEVANT_EXPECTED_PRICE);
//		ComputeTaxController.computeTax.handle(ctx);
//		//verify(ctx).status(HTTPRequestFactory.OK_STATUS);
//		verify(ctx).html("Profit for 1 piece of the product: 2.0$\nProfit for given amount of the product: 4.0$\n");
//		app.stop();
//	}
//


}
