package zpi.state;

import zpi.product.Product;
import zpi.sales.Category;

import java.util.HashMap;

public class USState {
	public class NotFoundTaxForThisCategory extends Exception{}
	
	private String name;
	private HashMap<Category, Double> taxForCategoryMap = new HashMap<>();
	
	
	public USState(String name) {
		this.name = name;
	}
	
	public Double getTaxForCategory(Category category) {
		return this.taxForCategoryMap.get(category);
	}
	
	public void addCategoryWithTax(Category category, Double tax) {
		this.taxForCategoryMap.put(category, tax);
	}
	
	public Double computeFinalPriceOfProduct(Product product) throws NotFoundTaxForThisCategory {
		if(!this.taxForCategoryMap.containsKey(product.getCategory())){
			throw new NotFoundTaxForThisCategory();
		}
		Double ratio = this.taxForCategoryMap.get(product.getCategory());
		Double basePrice = product.getBasePrice();

		if(basePrice < 0.0 || ratio < 0.0){
			throw new IllegalArgumentException("Base price and tax value should be positive.");
		}

		return ratio * basePrice + basePrice;
	}
	
	public String getName() {
		return name;
	}
}
