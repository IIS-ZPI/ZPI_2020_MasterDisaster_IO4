//package java.zpi.sales;

import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import zpi.category.Category;
import zpi.controllers.ComputeTaxController;
import zpi.product.Product;
import zpi.state.USState;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class HTTPRequestComputeTaxTest {
	private static Javalin app = null;

	private final String IRRELEVANT_AMOUNT = "2";
	private final String IRRELEVANT_BASE_PRICE = "1.0";
	private final String IRRELEVANT_EXPECTED_PRICE = "3.0";

	private static final String IRRELEVANT_PRODUCT_NAME = "Tomato";
	private static final String IRRELEVANT_STATE_NAME = "Alaska";

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

	@Test
	public void GET_toCheckStatusIfNoProductDataEntered() {
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.SIMPLE_TAX_URL);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST_STATUS);
	}

	@Test
	public void GET_toCheckStatusOfExistingProduct() throws Exception {
		generateContext(IRRELEVANT_PRODUCT_NAME,
						IRRELEVANT_STATE_NAME,
						IRRELEVANT_AMOUNT,
						IRRELEVANT_BASE_PRICE,
						IRRELEVANT_EXPECTED_PRICE);

		USState state = new USState(IRRELEVANT_STATE_NAME);
		Product product = new Product(IRRELEVANT_PRODUCT_NAME, Double.valueOf(IRRELEVANT_BASE_PRICE), Category.GROCERIES);
		product.setExpectedPrice(Double.valueOf(IRRELEVANT_EXPECTED_PRICE));
		var model = generateModel(state, product, Double.valueOf(IRRELEVANT_AMOUNT));

		ComputeTaxController.computeTax.handle(ctx);
		verify(ctx).status(HTTPRequestFactory.OK_STATUS);
//		verify(ctx).render(Paths.Template.SINGLE_RESULT, model);
	}

	@Test
	public void GET_toCheckStatusOfNonExistingProduct() throws Exception {
		generateContext("NonExistingName",
				IRRELEVANT_STATE_NAME,
				IRRELEVANT_AMOUNT,
				IRRELEVANT_BASE_PRICE,
				IRRELEVANT_EXPECTED_PRICE);
		ComputeTaxController.computeTax.handle(ctx);
		verify(ctx).status(HTTPRequestFactory.BAD_REQUEST_STATUS);
	}

	@Test
	public void GET_toCheckStatusIfProductAndStateAreEmptyStrings() throws Exception {
		generateContext("",
				"",
				IRRELEVANT_AMOUNT,
				IRRELEVANT_BASE_PRICE,
				IRRELEVANT_EXPECTED_PRICE);
		ComputeTaxController.computeTax.handle(ctx);
		verify(ctx).status(HTTPRequestFactory.BAD_REQUEST_STATUS);
	}

	@Test
	public void GET_toCheckStatusIfAmountBasePriceAndExpectedPriceAreEmptyStrings() throws Exception {
		generateContext(IRRELEVANT_PRODUCT_NAME,
				IRRELEVANT_STATE_NAME,
				"",
				"",
				"");
		Assert.assertThrows(NumberFormatException.class, () -> ComputeTaxController.computeTax.handle(ctx));
	}

	private void generateContext(String productName, String stateName, String amountOfProduct,
								 String basePrice, String expectedPrice){
		when(ctx.queryParam("product")).thenReturn(productName);
		when(ctx.queryParam("state")).thenReturn(stateName);
		when(ctx.queryParam("amount")).thenReturn(amountOfProduct);
		when(ctx.queryParam("base_price")).thenReturn(basePrice);
		when(ctx.queryParam("expected_price")).thenReturn(expectedPrice);
	}

	private Map<String, Object> generateModel(USState state, Product product, double amount){
		Map<String, Object> model = ViewUtil.baseModel(ctx);
		model.put("title", "CTC: Profit");
		model.put("profitForOnePiece", String.valueOf(state.computeProfit(product, amount)));
		model.put("profitForAmount", String.valueOf(state.computeProfit(product, amount) * amount));
		return model;
	}
}
