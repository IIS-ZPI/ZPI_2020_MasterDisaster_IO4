package zpi.state;

import zpi.category.Category;
import zpi.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MSSQUSStateDAO extends IUSStateDAO {
	private final Database database;
	List<USState> states = new LinkedList<>();
	
	public MSSQUSStateDAO(Database database) {
		super();
		this.database = database;
	}
	
	@Override
	public List<USState> getStates() {
		if (this.states.isEmpty()) {
			List<USState> states = new LinkedList<>();
			
			final String selectSql = "SELECT id, stateName FROM States;";
			
			try (Statement statement = database.createStatement();
			     ResultSet resultSet = statement.executeQuery(selectSql)) {
				
				while (resultSet.next()) {
					USState state = new USState(resultSet.getString("stateName"));
					selectTaxesForState(state);
					states.add(state);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			this.states = states;
		}
		
		return states;
	}
	
	
	@Override
	public Optional<USState> getUSStateByName(String name) {
		Optional<USState> state = Optional.empty();
		if (this.states.isEmpty()) {
			final String selectSql = String.format("SELECT id, stateName FROM States WHERE stateName='%s';", name);
			
			try (Statement statement = database.createStatement();
			     ResultSet resultSet = statement.executeQuery(selectSql)) {
				
				while (resultSet.next()) {
					state = Optional.of(new USState(resultSet.getString("stateName")));
					selectTaxesForState(state.get());
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			state = this.states.stream().filter(s->s.getName().equals(name)).findFirst();
		}
		return state;
	}
	
	@Override
	public void editCategoryBaseTax(USState state, Category category, Double taxRatio) {
		this.states.clear();
		
		final String updateTax = String.format("UPDATE Taxes SET tax=%f WHERE stateID=(SELECT id FROM States WHERE stateName='%s') and categoryID=(SELECT id FROM Categories WHERE category='%s');",
				taxRatio, state.getName(), category);
		
		try {
			database.createStatement().executeUpdate(updateTax);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void editCategoryValueWithoutTax(USState state, Category category, Double valueWithoutTax) {
		this.states.clear();

		final String updateTax = String.format("UPDATE Taxes SET valueWithoutTax=%f WHERE stateID=(SELECT id FROM States WHERE stateName='%s') and categoryID=(SELECT id FROM Categories WHERE category='%s');",
				valueWithoutTax, state.getName(), category);
		
		try {
			database.createStatement().executeUpdate(updateTax);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void selectTaxesForState(USState state) throws SQLException {
		final String selectTaxes = String.format("SELECT tax, valueWithoutTax, category from Taxes LEFT JOIN States ON States.id=Taxes.stateID LEFT JOIN Categories ON Categories.id=Taxes.categoryID WHERE stateName='%s'", state.getName());
		
		try (Statement statement = database.createStatement();
		     ResultSet resultSet = statement.executeQuery(selectTaxes)) {
			while (resultSet.next()) {
				state.editCategoryTax(Category.valueOf(resultSet.getString("category")), new Tax(resultSet.getDouble("tax"), resultSet.getDouble("valueWithoutTax")));
			}
		}
	}
}
