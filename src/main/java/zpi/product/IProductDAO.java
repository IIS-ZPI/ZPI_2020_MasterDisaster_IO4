package zpi.product;

import zpi.category.Category;

import java.util.List;
import java.util.Optional;

public abstract class IProductDAO {
	//Product name have to unique
	//TODO add expcetion about ^
	abstract public Product addProduct(String productName, Category category, double basePrice) throws ProductDuplicateException;
	
	abstract public Product getProduct(String productName) throws ProductDoesNotExistException;
	
	abstract public List<Product> getProducts();
	
	
	abstract public void updateProductBasePrice(String productName, double basePrice) throws ProductDoesNotExistException;
	
	abstract public void updateProductBasePrice(Product product, double basePrice) throws ProductDoesNotExistException;
	
	abstract public void removeProduct(String productName) throws ProductDoesNotExistException;
	
	abstract public void removeProduct(Product product) throws ProductDoesNotExistException;
	
}
