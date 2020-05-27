import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import zpi.product.Product;
import zpi.category.Category;
import zpi.state.USState;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ProfitProductPriceTests {
	private static final String PRODUCT_NAME = "TestProductName";
	private static final String STATE_NAME = "TestStateName";
	private static final Category CATEGORY = Category.GROCERIES;

	private static final double BASIC_TAX = 0.05;
	private static final double POSITIVE_BASE_PRICE = 20.0;
	private static final double POSITIVE_EXPECTED_PRICE = 30.0;
	private static final double ZERO = 0.0;

	private USState state;
	private static Product product;

	@Parameterized.Parameters
	public static Collection doubleValues() {
		return Arrays.asList(new Double[][] {
				{ BASIC_TAX, POSITIVE_EXPECTED_PRICE, 8.5 },
				{ 0.2, 21.0, -3.2 },
				{ 0.23, 45.0, 14.65 },
				{ 0.07, 25.5, 3.715 },
				{ 0.03, 30.5, 9.585 },
				{ BASIC_TAX, -POSITIVE_EXPECTED_PRICE, -48.5 },
				{ BASIC_TAX, 0.0, -POSITIVE_BASE_PRICE },
				{ BASIC_TAX, Double.MIN_VALUE, -POSITIVE_BASE_PRICE },
				{ BASIC_TAX, Double.MAX_VALUE, 1.7078084781191998E308 }
		});
	}

	@Parameterized.Parameter public double tax;
	@Parameterized.Parameter(1) public double expectedPrice;
	@Parameterized.Parameter(2) public double profit;
	
	@Before
	public void setup() {
		state = new USState(STATE_NAME);
		product = new Product(PRODUCT_NAME);
	}
	
	@Test
	public void checkProfitIfBasePriceIsPositive() {
		product.setCategory(CATEGORY);
		product.setBasePrice(POSITIVE_BASE_PRICE);
		product.setExpectedPrice(expectedPrice);

		state.editCategoryTax(CATEGORY, tax);

		assertEquals(Double.valueOf(profit), state.computeProfit(product));
	}
	
	@Test
	public void checkProfitIfBasePriceIsNegative() {
		product.setCategory(CATEGORY);
		product.setBasePrice(-POSITIVE_BASE_PRICE);
		product.setExpectedPrice(expectedPrice);

		state.editCategoryTax(CATEGORY, tax);

		assertThrows(IllegalArgumentException.class, () -> state.computeProfit(product));
	}

	@Test
	public void checkProfitIfBasePriceIsZero() {
		product.setCategory(CATEGORY);
		product.setBasePrice(ZERO);
		product.setExpectedPrice(expectedPrice);

		state.editCategoryTax(CATEGORY, tax);

		assertEquals(Double.valueOf(expectedPrice - tax * expectedPrice), state.computeProfit(product));
	}

	@Test
	public void checkProfitIfTaxIsNegative() {
		product.setCategory(CATEGORY);
		product.setBasePrice(-POSITIVE_BASE_PRICE);
		product.setExpectedPrice(expectedPrice);

		state.editCategoryTax(CATEGORY, -tax);

		assertThrows(IllegalArgumentException.class, () -> state.computeProfit(product));
	}

	//special tests

	@Test
	public void checkProfitIfBaseAndExpectedPriceAreMaxDouble() {
		product.setCategory(CATEGORY);
		product.setBasePrice(Double.MAX_VALUE);
		product.setExpectedPrice(Double.MAX_VALUE);

		state.editCategoryTax(CATEGORY, BASIC_TAX);
		assertEquals(Double.valueOf(-8.988465674311579E306), state.computeProfit(product));
	}

	@Test
	public void checkProfitIfBasePriceIsMaxDoubleAndExpectedPriceIsPositive() {
		product.setCategory(CATEGORY);
		product.setBasePrice(Double.MAX_VALUE);
		product.setExpectedPrice(POSITIVE_EXPECTED_PRICE);

		state.editCategoryTax(CATEGORY, BASIC_TAX);
		assertEquals(Double.valueOf(-1.7976931348623157E308), state.computeProfit(product));
	}

	@Test
	public void checkProfitIfBaseAndExpectedPriceAreMinDouble() {
		product.setCategory(CATEGORY);
		product.setBasePrice(Double.MIN_VALUE);
		product.setExpectedPrice(Double.MIN_VALUE);

		state.editCategoryTax(CATEGORY, BASIC_TAX);
		assertEquals(Double.valueOf(ZERO), state.computeProfit(product));
	}

	@Test
	public void checkProfitIfBasePriceIsMinDoubleAndExpectedPriceIsPositive() {
		product.setCategory(CATEGORY);
		product.setBasePrice(Double.MIN_VALUE);
		product.setExpectedPrice(POSITIVE_EXPECTED_PRICE);

		state.editCategoryTax(CATEGORY, BASIC_TAX);
		assertEquals(Double.valueOf(28.5), state.computeProfit(product));
	}

	//behaviour tests

	@Test
	public void checkProfitIfProductIsNull() {
		state.editCategoryTax(CATEGORY, tax);
		assertThrows(NullPointerException.class, () -> state.computeProfit(null));
	}
	
	@Test
	public void checkProfitIfTaxIsNull() {
		product.setCategory(CATEGORY);
		product.setBasePrice(POSITIVE_BASE_PRICE);
		product.setExpectedPrice(expectedPrice);

		state.editCategoryTax(CATEGORY, null);
		assertThrows(NullPointerException.class, () -> state.computeProfit(product));
	}
	
	@Test
	public void checkProfitIfBothCategoriesAreNull() throws Exception {
		product.setBasePrice(POSITIVE_BASE_PRICE);
		product.setExpectedPrice(expectedPrice);

		state.editCategoryTax(null, tax);
		assertEquals(Double.valueOf(profit), state.computeProfit(product));
	}
}
