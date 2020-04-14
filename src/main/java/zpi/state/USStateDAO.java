package zpi.state;

import zpi.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class USStateDAO {
	private static final USStateDAO INSTANCE = new USStateDAO();
	List<USState> states = new ArrayList<>();
	void prepareTempData() {
		USState alabamaState = new USState("Alabama");
		USState alaskaState = new USState("Alaska");
		USState arizonaState = new USState("Arizona");
		
		alabamaState.addCategoryWithTax(Category.GROCERIES, Double.valueOf(4));
		alabamaState.addCategoryWithTax(Category.PREPARED_FOOD, Double.valueOf(5));
		
		states.add(alabamaState);
		states.add(alaskaState);
		states.add(arizonaState);
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
				.filter(e -> e.getName().toLowerCase().equals(name.toLowerCase()))
				.findAny();
	}
}
