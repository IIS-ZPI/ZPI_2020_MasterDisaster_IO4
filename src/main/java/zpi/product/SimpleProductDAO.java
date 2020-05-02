package zpi.product;

import zpi.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleProductDAO extends IProductDAO {
	private final List<Product> products = new ArrayList<>();
	
	public SimpleProductDAO() {
		prepareTempData();
	}
	
	
	void prepareTempData() {
		products.add(new Product("Chocolate bar", 1, Category.PREPARED_FOOD));
		products.add(new Product("Milk", 2, Category.PREPARED_FOOD));
		products.add(new Product("Tomato", 0.5, Category.GROCERIES));
	}
	
	@Override
	public List<Product> getProducts() {
		return products;
	}
	
	@Override
	public Product addProduct(String productName, Category category, double basePrice) throws ProductDuplicateException {
		Product newProduct = null;
		try {
			newProduct = getProduct(productName);
		} catch (ProductDoesNotExistException ignored) {
		}
		
		if (null == newProduct) {
			newProduct = new Product(productName, basePrice, category);
			this.products.add(newProduct);
		} else {
			throw new ProductDuplicateException();
		}
		
		return newProduct;
	}
	
	public Product getProduct(String name) throws ProductDoesNotExistException {
		var optProduct = products.stream()
				.filter(e -> e.getName().equals(name))
				.findAny();
		if (optProduct.isPresent()) {
			return optProduct.get();
		}
		
		throw new ProductDoesNotExistException();
	}
	
	@Override
	public void removeProduct(String productName) throws ProductDoesNotExistException {
		if (!this.products.removeIf(product -> product.getName().equalsIgnoreCase(productName))) {
			throw new ProductDoesNotExistException();
		}
	}
	
	@Override
	public void updateProductBasePrice(String productName, double basePrice) throws ProductDoesNotExistException {
		var product = getProduct(productName);
		product.setBasePrice(basePrice);
	}
	
	@Override
	public void removeProduct(Product product) throws ProductDoesNotExistException {
		if (!this.products.remove(product)) {
			throw new ProductDoesNotExistException();
		}
	}
	
	@Override
	public void updateProductBasePrice(Product product, double basePrice) throws ProductDoesNotExistException {
		if (!products.contains(product)) throw new ProductDoesNotExistException();
		
		products.stream().filter(product::equals).findAny().get().setBasePrice(basePrice);
	}
}
