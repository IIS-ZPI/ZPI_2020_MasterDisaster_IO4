package zpi.state;

import zpi.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleUSStateDAO extends IUSStateDAO {
	public SimpleUSStateDAO() {
		prepareTempData();
	}
	
	List<USState> states = new ArrayList<>();
	
	void prepareTempData() {
		USState alabamaState = new USState("Alabama");
		USState alaskaState = new USState("Alaska");
		USState arizonaState = new USState("Arizona");
		
		alabamaState.editCategoryTax(Category.GROCERIES, 0.04);
		alabamaState.editCategoryTax(Category.PREPARED_FOOD, 0.05);
		
		states.add(alabamaState);
		states.add(alaskaState);
		states.add(arizonaState);
	}
	
	@Override
	public List<USState> getStates() {
		return states;
	}
	
	@Override
	public void addUSState(String stateName) {
		this.states.add(new USState(stateName));
	}
	
	@Override
	public Optional<USState> getUSStateByName(String name) {
		return states.stream()
				.filter(e -> e.getName().toLowerCase().equals(name.toLowerCase()))
				.findAny();
	}
	
	@Override
	public void editCategoryTax(USState state, Category category, Double taxRatio) {
		state.editCategoryTax(category, taxRatio);
	}
}
