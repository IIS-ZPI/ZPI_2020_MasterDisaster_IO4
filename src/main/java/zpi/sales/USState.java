package zpi.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class USState {
	class NotFoundTaxForThisCategory extends Exception{}
	
	private String name;
	private HashMap<Category, Double> taxForCategoryMap;
	
	
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
		
		return ratio * product.getBasePrice() + product.getBasePrice();
	}
}
