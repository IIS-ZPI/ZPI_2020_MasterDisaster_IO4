package zpi.product;

import zpi.category.Category;

import java.util.Objects;

public class Product {
	private String name;
	private double basePrice;
	private Category category;
	private double expectedPrice;
	
	public Product(String name) {
		this.name = name;
	}
	
	public Product(String name, double basePrice, Category category) {
		this.name = name;
		this.basePrice = basePrice;
		this.category = category;
		this.expectedPrice = 0.0;
	}
	
	public double getBasePrice() {
		return basePrice;
	}
	
	public double getExpectedPrice() {
		return expectedPrice;
	}
	
	public void setExpectedPrice(double expected_price) {
		this.expectedPrice = expected_price;
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
		return Objects.equals(name, product.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, getBasePrice(), getCategory());
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNameWithoutSpaces() {
		return this.name.replace(" ", "_");
	}
}