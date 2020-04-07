package zpi.daos;

import zpi.sales.Category;
import zpi.sales.Product;
import zpi.sales.USState;

import java.util.ArrayList;
import java.util.List;

public class DAO {
	private static final DAO INSTANCE = new DAO();
	
	
	List<Product> products = new ArrayList<>();
	List<USState> states = new ArrayList<>();
	void prepareTempData() {
		Category groceries = new Category("Groceries");
		Category preparedFood = new Category("Prepared food");
		
		USState alabamaState = new USState("Alabama");
		
		alabamaState.addCategoryWithTax(groceries, Double.valueOf(4));
		alabamaState.addCategoryWithTax(preparedFood, Double.valueOf(5));
		
		states.add(alabamaState);
		
		products.add(new Product("Chocolate bar", 15, preparedFood));
		products.add(new Product("Milk", 10, preparedFood));
		products.add(new Product("Tomato", 3, groceries));
	}
	
	private DAO(){
		prepareTempData();
	}
	
	public static DAO getInstance(){
		return INSTANCE;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public List<USState> getStates() {
		return states;
	}
}
