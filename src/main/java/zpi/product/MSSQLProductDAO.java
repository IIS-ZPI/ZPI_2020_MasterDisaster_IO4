package zpi.product;

import zpi.category.Category;
import zpi.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MSSQLProductDAO extends IProductDAO {
	private Database database;
	
	public MSSQLProductDAO(Database database) {
		super();
		this.database = database;
	}
	
	@Override
	public Product addProduct(String productName, Category category, double basePrice) throws ProductDuplicateException {
		try {
			if (getProduct(productName) != null) throw new ProductDuplicateException();
		} catch (ProductDoesNotExistException ignored) {
		}
		
		Product product = null;
		String insertSql = String.format("INSERT INTO Products VALUES ('%s', %f, %s);", productName, basePrice, category.getValue());
		
		try {
			database.createStatement().executeUpdate(insertSql);
			product = new Product(productName, basePrice, category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return product;
	}
	
	@Override
	public Product getProduct(String productName) throws ProductDoesNotExistException {
		Product product = null;
		final String selectSql = String.format("SELECT productName, basePrice, category FROM Products AS p" +
				"\nLEFT JOIN Categories AS c" +
				"\nON p.categoryID = c.id" +
				" WHERE productName = '%s';", productName);
		
		try (Statement statement = database.createStatement();
		     ResultSet resultSet = statement.executeQuery(selectSql)) {
			
			String productNameFromDb = "";
			double basePriceFromDb = 0;
			String categoryFromDb = "";
			while (resultSet.next()) {
				productNameFromDb = resultSet.getString("productName");
				basePriceFromDb = resultSet.getDouble("basePrice");
				categoryFromDb = resultSet.getString("category");
				product = new Product(productNameFromDb, basePriceFromDb, Category.valueOf(categoryFromDb));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (product == null) throw new ProductDoesNotExistException();
		
		return product;
	}
	
	@Override
	public List<Product> getProducts() {
		List<Product> products = new LinkedList<>();
		
		final String selectSql = "SELECT productName, basePrice, category FROM Products AS p" +
				"\nLEFT JOIN Categories AS c" +
				"\nON p.categoryID = c.id;";
		
		try (Statement statement = database.createStatement();
		     ResultSet resultSet = statement.executeQuery(selectSql)) {
			
			String productName = "";
			double basePrice = 0;
			String category = "";
			while (resultSet.next()) {
				productName = resultSet.getString("productName");
				basePrice = resultSet.getDouble("basePrice");
				category = resultSet.getString("category");
				products.add(new Product(productName, basePrice, Category.valueOf(category)));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return products;
	}
	
	@Override
	public void updateProductBasePrice(String productName, double basePrice) throws ProductDoesNotExistException {
		if (getProduct(productName) == null) return;
		
		Product product = null;
		String updateSQL = String.format("UPDATE Products SET basePrice=%f WHERE productName = '%s'", basePrice, productName);
		
		try {
			database.createStatement().executeUpdate(updateSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateProductCategory(String productName, Category category) throws ProductDoesNotExistException {
		if (getProduct(productName) == null) return;
		
		Product product = null;
		String updateSQL = String.format("UPDATE Products SET categoryID=(SELECT id FROM Categories WHERE category='%s') WHERE productName='%s'", category.name(), productName);

		try {
			database.createStatement().executeUpdate(updateSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeProduct(String productName) throws ProductDoesNotExistException {
		if (getProduct(productName) == null) return;
		
		Product product = null;
		String deleteSQl = String.format("DELETE FROM Products WHERE productName = '%s'", productName);
		
		try {
			database.createStatement().executeUpdate(deleteSQl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeProduct(Product product) throws ProductDoesNotExistException {
		removeProduct(product.getName());
	}
}
