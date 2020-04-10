package zpi.product;

import zpi.sales.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {
	private final List<Product> products = new ArrayList<>();
	private final static ProductDAO INSTANCE = new ProductDAO();
	
	private ProductDAO(){
		prepareTempData();
	}
	
	public static ProductDAO getInstance(){
		return INSTANCE;
	}
	
	void prepareTempData() {
		Category groceries = new Category("Groceries");
		Category preparedFood = new Category("Prepared food");
		
		products.add(new Product("Chocolate bar", 15, preparedFood));
		products.add(new Product("Milk", 10, preparedFood));
		products.add(new Product("Tomato", 3, groceries));
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public Optional<Product> getProductByName(String name){
		return products.stream()
				.filter(e -> e.getName().equals(name))
				.findAny();
	}
}
