package test.zpi.sales;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.product.Product;
import zpi.category.Category;
import zpi.state.USState;

public class ProfitProductPriceTests {
    private USState state;
    private static Category category;
    private static Product productPositivePrice;
    private static Product productNegativePrice;
    private static Product productZeroPrice;
    private static Product productMaxDoublePrice;
    private static Product productMinDoublePrice;
    private static Product productCategoryNull;
    private static Product productOtherCategory;
    private static final double BASIC_TAX = Double.valueOf(0.05);
    private static final double POSITIVE_BASE_PRICE = 20.0;
    private static final double POSITIVE_EXPECTED_PRICE = 30.0;
    private static final double ZERO = 0.0;

    @BeforeClass
    public static void init(){
        category = Category.GROCERIES;
        productPositivePrice = new Product("TestProductName", POSITIVE_BASE_PRICE , category);
        productNegativePrice = new Product("TestProductName", -POSITIVE_BASE_PRICE , category);
        productZeroPrice = new Product("TestProductName", ZERO, category);
        productMaxDoublePrice = new Product("TestProductName", Double.MAX_VALUE, category);
        productMinDoublePrice = new Product("TestProductName", Double.MIN_VALUE, category);
        productCategoryNull = new Product("TestProductName", POSITIVE_BASE_PRICE , null);
        productOtherCategory = new Product("TestProductName", POSITIVE_BASE_PRICE , Category.PREPARED_FOOD);
    }

    @Before
    public void clean(){
        state = new USState("TestState");
    }

    @Test
    public void checkIfBaseAndExpectedPriceArePositive() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productPositivePrice.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(8.5), state.computeProfit(productPositivePrice));
    }
    @Test
    public void checkIfBasePriceIsPositiveAndExpectedPriceIsNegative() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productPositivePrice.setExpectedPrice(-POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(-48.5), state.computeProfit(productPositivePrice));
    }

    @Test
    public void checkIfBaseAndExpectedPriceAreNegative() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productNegativePrice.setExpectedPrice(-POSITIVE_EXPECTED_PRICE);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeProfit(productNegativePrice));
    }

    @Test
    public void checkIfBasePriceIsNegativeAndExpectedPriceIsPositive() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productNegativePrice.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeProfit(productNegativePrice));
    }

    @Test
    public void checkIfBaseAndExpectedPriceAreZero() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productZeroPrice.setExpectedPrice(ZERO);
        Assert.assertEquals(Double.valueOf(0.0), state.computeProfit(productZeroPrice));
    }
    @Test
    public void checkIfBasePriceIsPositiveAndExpectedPriceIsZero() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productPositivePrice.setExpectedPrice(ZERO);
        Assert.assertEquals(Double.valueOf(-20.0), state.computeProfit(productPositivePrice));
    }

    @Test
    public void checkIfBasePriceIsZeroAndExpectedPriceIsNegative() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productZeroPrice.setExpectedPrice(-POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(-28.5), state.computeProfit(productZeroPrice));
    }

    @Test
    public void checkIfBasePriceIsZeroAndExpectedPriceIsPositive() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productZeroPrice.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(28.5), state.computeProfit(productZeroPrice));
    }

    @Test
    public void checkIfBasePriceIsNegativeAndExpectedPriceIsZero() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productNegativePrice.setExpectedPrice(ZERO);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeProfit(productNegativePrice));
    }

    @Test
    public void checkIfTaxIsNegative() throws Exception {
        state.editCategoryTax(category, -BASIC_TAX);
        productNegativePrice.setExpectedPrice(-POSITIVE_EXPECTED_PRICE);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeProfit(productNegativePrice));
    }

    @Test
    public void checkIfBaseAndExpectedPriceAreMaxDouble() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productMaxDoublePrice.setExpectedPrice(Double.MAX_VALUE);
        Assert.assertEquals(Double.valueOf(-8.988465674311579E306), state.computeProfit(productMaxDoublePrice));
    }

    @Test
    public void checkIfBasePriceIsMaxDoubleAndExpectedPriceIsPositive() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productMaxDoublePrice.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(-1.7976931348623157E308), state.computeProfit(productMaxDoublePrice));
    }
    @Test
    public void checkIfBasePriceIsPositiveAndExpectedPriceIsMaxDouble() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productPositivePrice.setExpectedPrice(Double.MAX_VALUE);
        Assert.assertEquals(Double.valueOf(1.7078084781191998E308), state.computeProfit(productPositivePrice));
    }

    @Test
    public void checkIfBaseAndExpectedPriceAreMinDouble() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productMinDoublePrice.setExpectedPrice(Double.MIN_VALUE);
        Assert.assertEquals(Double.valueOf(0.0), state.computeProfit(productMinDoublePrice));
    }

    @Test
    public void checkIfBasePriceIsMinDoubleAndExpectedPriceIsPositive() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productMinDoublePrice.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(28.5), state.computeProfit(productMinDoublePrice));
    }
    @Test
    public void checkIfBasePriceIsPositiveAndExpectedPriceIsMinDouble() throws Exception {
        state.editCategoryTax(category, BASIC_TAX);
        productPositivePrice.setExpectedPrice(Double.MIN_VALUE);
        Assert.assertEquals(Double.valueOf(-20.0), state.computeProfit(productPositivePrice));
    }

    @Test
    public void checkIfProductIsNull() {
        state.editCategoryTax(category, BASIC_TAX);
        Assert.assertThrows(NullPointerException.class, () -> state.computeProfit(null));
    }

    @Test
    public void checkIfTaxIsNull() {
        state.editCategoryTax(category, null);
        productPositivePrice.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertThrows(NullPointerException.class, () -> state.computeProfit(productPositivePrice));
    }

    @Test
    public void checkIfBothCategoriesAreNull() throws Exception {
        state.editCategoryTax(null, BASIC_TAX);
        productCategoryNull.setExpectedPrice(POSITIVE_EXPECTED_PRICE);
        Assert.assertEquals(Double.valueOf(8.5), state.computeProfit(productCategoryNull));
    }
}
