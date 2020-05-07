package zpi.product;

public class ProductDoesNotExistException extends Exception {
	public ProductDoesNotExistException() {
	}
	
	public ProductDoesNotExistException(String message) {
		super(message);
	}
}
