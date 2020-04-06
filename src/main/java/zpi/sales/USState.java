package zpi.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class USState {
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
}
