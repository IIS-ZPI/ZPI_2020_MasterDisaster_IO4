package zpi.state;

import zpi.category.Category;

import java.util.List;
import java.util.Optional;

public abstract class IUSStateDAO {
	
	abstract public List<USState> getStates();
	
	abstract public Optional<USState> getUSStateByName(String name);
	
	abstract public void editCategoryBaseTax(USState state, Category category, Double taxRatio);
	
	abstract public void editCategoryValueWithoutTax(USState state, Category category, Double valueWithoutTax);
}
