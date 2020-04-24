import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.product.Product;
import zpi.sales.Category;
import zpi.state.USState;

public class FinalProductPriceTest {
    private USState state;
    private static Category category;
    private static Product productPositivePrice;
    private static Product productNegativePrice;
    private static Product productMaxDoublePrice;
    private static Product productCategoryNull;
    private static Product productOtherCategory;
    private final double BASIC_TAX = Double.valueOf(0.05);

    @BeforeClass
    public static void init(){
        category = new Category("TestCategoryName");
        productPositivePrice = new Product("TestProductName", 20.0, category);
        productNegativePrice = new Product("TestProductName", -20.0, category);
        productMaxDoublePrice = new Product("TestProductName", Double.MAX_VALUE, category);
        productCategoryNull = new Product("TestProductName", 20, null);
        productOtherCategory = new Product("TestProductName", 20, new Category("TestCategoryName2"));
    }

    @Before
    public void clean(){
        state = new USState("TestState");
    }

    @Test
    public void testIfBasePriceIsPositiveTest() throws Exception {
        state.addCategoryWithTax(category, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(21.0), state.computeFinalPriceOfProduct(productPositivePrice));
    }

    @Test
    public void checkIfBasePriceIsNegative() throws Exception {
        state.addCategoryWithTax(category, BASIC_TAX);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeFinalPriceOfProduct(productNegativePrice));
    }

    @Test
    public void checkIfTaxIsNegative() throws Exception {
        state.addCategoryWithTax(category, -BASIC_TAX);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeFinalPriceOfProduct(productNegativePrice));
    }

    @Test
    public void checkIfTaxMapIsEmpty() {
        Assert.assertThrows(USState.NotFoundTaxForThisCategory.class, () -> state.computeFinalPriceOfProduct(productPositivePrice));
    }

    @Test
    public void checkIfNoCategoryInTaxMap() {
        state.addCategoryWithTax(category, BASIC_TAX);
        Assert.assertThrows(USState.NotFoundTaxForThisCategory.class, () -> state.computeFinalPriceOfProduct(productOtherCategory));
    }

    @Test
    public void checkIfBasePriceIsMaxDouble() throws Exception {
        state.addCategoryWithTax(category, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), state.computeFinalPriceOfProduct(productMaxDoublePrice));
    }

    @Test
    public void checkIfProductIsNull() {
        state.addCategoryWithTax(category, BASIC_TAX);
        Assert.assertThrows(NullPointerException.class, () -> state.computeFinalPriceOfProduct(null));
    }

    @Test
    public void checkIfTaxIsNull() {
        state.addCategoryWithTax(category, null);
        Assert.assertThrows(NullPointerException.class, () -> state.computeFinalPriceOfProduct(productPositivePrice));
    }

    @Test
    public void checkIfCategoryInProductIsNull() {
        state.addCategoryWithTax(category, BASIC_TAX);
        Assert.assertThrows(USState.NotFoundTaxForThisCategory.class, () -> state.computeFinalPriceOfProduct(productCategoryNull));
    }

    @Test
    public void checkIfBothCategoriesAreNull() throws Exception {
        state.addCategoryWithTax(null, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(21.0), state.computeFinalPriceOfProduct(productCategoryNull));
    }
}
