package zpi.state;

import zpi.category.Category;
import zpi.product.Product;

import java.util.HashMap;

public class USState {
	private String name;
	private HashMap<Category, Tax> taxForCategoryMap = new HashMap<>();
	
	
	public USState(String name) {
		this.name = name;
		for (var c : Category.values()) {
			this.taxForCategoryMap.put(c, new Tax());
		}
	}
	
	public Tax getTaxForCategory(Category category) {
		return this.taxForCategoryMap.get(category);
	}
	
	public void editCategoryTax(Category category, Tax tax) {
		this.taxForCategoryMap.put(category, tax);
	}
	
	public Double computeProfit(Product product) {
		return computeProfit(product, 1);
	}

	public Double computeProfit(Product product, double amount) {
		Double basePrice = product.getBasePrice();
		Double expectedPrice = product.getExpectedPrice();
		Double ratio = 0.0;
		if (basePrice < 0.0) {
			throw new IllegalArgumentException("Base price value should be positive.");
		}
		Double rateWithoutTax = this.taxForCategoryMap.get(product.getCategory()).valueWithoutTax;

		if(expectedPrice * amount >= rateWithoutTax) {
			ratio = this.taxForCategoryMap.get(product.getCategory()).baseTax;
		}

		return expectedPrice - basePrice - (expectedPrice * ratio);
	}

	public String getName() {
		return name;
	}
}
