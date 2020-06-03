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
		Double ratio = this.taxForCategoryMap.get(product.getCategory()).baseTax;
		Double basePrice = product.getBasePrice();
		Double expectedPrice = product.getExpectedPrice();
		
		if (basePrice < 0.0 || ratio < 0.0) {
			throw new IllegalArgumentException("Base price and tax value should be positive.");
		}
		
		return expectedPrice - basePrice - (expectedPrice * ratio);
	}
	
	public String getName() {
		return name;
	}
}
