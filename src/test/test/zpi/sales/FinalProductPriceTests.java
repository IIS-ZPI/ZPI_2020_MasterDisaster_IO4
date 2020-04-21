package test.zpi.sales;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.category.Category;
import zpi.product.Product;
import zpi.state.USState;

public class FinalProductPriceTests {
    private static USState state;
    private static Product productPositivePrice;
    private static Product productNegativePrice;
    private static Product productMaxDoublePrice;
    private static Product productCategoryNull;
    private static Product productOtherCategory;
    private final double BASIC_TAX = Double.valueOf(0.05);

    @BeforeClass
    public static void init(){
        state = new USState("TestState");
        productPositivePrice = new Product("TestProductName", 20.0, Category.GROCERIES);
        productNegativePrice = new Product("TestProductName", -20.0, Category.GROCERIES);
        productMaxDoublePrice = new Product("TestProductName", Double.MAX_VALUE, Category.GROCERIES);
        productCategoryNull = new Product("TestProductName", 20, null);
        productOtherCategory = new Product("TestProductName", 20,Category.CLOTHING);
    }

    @Test
    public void checkIfBasePriceIsPositive() {
        state.editCategoryTax(Category.GROCERIES, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(21.0), state.computeFinalPriceOfProduct(productPositivePrice));
    }

    @Test
    public void checkIfBasePriceIsNegative() {
        state.editCategoryTax(Category.GROCERIES, BASIC_TAX);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeFinalPriceOfProduct(productNegativePrice));
    }

    @Test
    public void checkIfTaxIsNegative() {
        state.editCategoryTax(Category.GROCERIES, -BASIC_TAX);
        Assert.assertThrows(IllegalArgumentException.class, () -> state.computeFinalPriceOfProduct(productNegativePrice));
    }

    @Test
    public void checkIfTaxIsZero() {
        state.editCategoryTax(Category.GROCERIES, 0.0);
        Assert.assertEquals(Double.valueOf(20.0), state.computeFinalPriceOfProduct(productPositivePrice));
    }

    @Test
    public void checkIfNoCategoryInTaxMap() {
        state.editCategoryTax(Category.GROCERIES, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(20.0), state.computeFinalPriceOfProduct(productOtherCategory));
    }

    @Test
    public void checkIfBasePriceIsMaxDouble() {
        state.editCategoryTax(Category.GROCERIES, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), state.computeFinalPriceOfProduct(productMaxDoublePrice));
    }

    @Test
    public void checkIfProductIsNull() {
        state.editCategoryTax(Category.GROCERIES, BASIC_TAX);
        Assert.assertThrows(NullPointerException.class, () -> state.computeFinalPriceOfProduct(null));
    }

    @Test
    public void checkIfTaxIsNull() {
        state.editCategoryTax(Category.GROCERIES, null);
        Assert.assertThrows(RuntimeException.class, () -> state.computeFinalPriceOfProduct(productPositivePrice));
    }

    @Test
    public void checkIfCategoryInProductIsNull() {
        state.editCategoryTax(Category.GROCERIES, BASIC_TAX);
        Assert.assertThrows(NullPointerException.class, () -> state.computeFinalPriceOfProduct(productCategoryNull));
    }

    @Test
    public void checkIfBothCategoriesAreNull() {
        state.editCategoryTax(null, BASIC_TAX);
        Assert.assertEquals(Double.valueOf(21.0), state.computeFinalPriceOfProduct(productCategoryNull));
    }
}
