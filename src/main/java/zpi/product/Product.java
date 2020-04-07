package zpi.product;

import zpi.sales.Category;

import java.util.Objects;

public class Product {
	private String name;
	private double basePrice;
	private Category category;
	
	public Product(String name) {
		this.name = name;
	}
	
	public Product(String name, double basePrice, Category category) {
		this.name = name;
		this.basePrice = basePrice;
		this.category = category;
	}
	
	public double getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Double.compare(product.getBasePrice(), getBasePrice()) == 0 &&
				Objects.equals(name, product.name) &&
				Objects.equals(getCategory(), product.getCategory());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, getBasePrice(), getCategory());
	}
	
	public String getName(){
		return this.name;
	}
}
