package zpi.state;

import zpi.category.Category;
import zpi.product.Product;

import java.util.HashMap;

public class USState {
	private String name;
	private HashMap<Category, Double> taxForCategoryMap = new HashMap<>();
	
	
	public USState(String name) {
		this.name = name;
		for(var c : Category.values()){
			this.taxForCategoryMap.put(c, 0.);
		}
	}
	
	public Double getTaxForCategory(Category category) {
		return this.taxForCategoryMap.get(category);
	}
	
	public void editCategoryTax(Category category, Double tax) {
		this.taxForCategoryMap.put(category, tax);
	}
	
	public Double computeFinalPriceOfProduct(Product product){
		Double ratio = this.taxForCategoryMap.get(product.getCategory());
		Double basePrice = product.getBasePrice();
		if(basePrice < 0.0 || ratio < 0.0) throw new IllegalArgumentException("Base price and tax value should be positive.");

		return ratio * basePrice + basePrice;
	}
	
	public String getName() {
		return name;
	}
}
