package zpi.state;

import zpi.product.Product;
import zpi.sales.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class USStateDAO {
	private static final USStateDAO INSTANCE = new USStateDAO();
	List<USState> states = new ArrayList<>();
	void prepareTempData() {
		Category groceries = new Category("Groceries");
		Category preparedFood = new Category("Prepared food");
		
		USState alabamaState = new USState("Alabama");
		
		alabamaState.addCategoryWithTax(groceries, Double.valueOf(4));
		alabamaState.addCategoryWithTax(preparedFood, Double.valueOf(5));
		
		states.add(alabamaState);
	}
	
	private USStateDAO(){
		prepareTempData();
	}
	
	public static USStateDAO getInstance(){
		return INSTANCE;
	}
	
	public List<USState> getStates() {
		return states;
	}
	
	public Optional<USState> getUSStateByName(String name){
		return states.stream()
				.filter(e -> e.getName().equals(name))
				.findAny();
	}
}
