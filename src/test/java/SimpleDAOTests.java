package test.zpi.sales;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zpi.category.Category;
import zpi.product.*;
import zpi.state.IUSStateDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USState;

public class SimpleDAOTests {
	//input
	private static final String EXISTING_NAME = "EXISTING_NAME";
	private static final String NON_EXISTING_NAME = "NON_EXISTING_NAME";
	private static final Category irrelevantCategory = Category.GROCERIES;
	private static final Category newCategory = Category.CLOTHING;
	private static final double irrelevantPrice = 1.0;
	private static final double newBasePrice = 2.0;

	private static IUSStateDAO stateDAO;
	private static IProductDAO productDAO;
	
	private static USState state;
	private static Product product;

	@BeforeClass
	public static void init() {
		stateDAO = new SimpleUSStateDAO();
		productDAO = new SimpleProductDAO();

		state = new USState(EXISTING_NAME);

		product = new Product(EXISTING_NAME);
		product.setCategory(irrelevantCategory);
		product.setBasePrice(irrelevantPrice);
	}

	@Before
	public void clean(){
		productDAO.getProducts().clear();
	}

	@Test
	public void checkIfProductExistsInProductDAO() throws ProductDoesNotExistException {
		addTestingProduct();
		Product product = productDAO.getProduct(EXISTING_NAME);
		Assert.assertEquals(test.zpi.sales.SimpleDAOTests.product.getName(), product.getName());
		Assert.assertEquals(test.zpi.sales.SimpleDAOTests.product.getCategory(), product.getCategory());
		Assert.assertEquals(test.zpi.sales.SimpleDAOTests.product.getBasePrice(), product.getBasePrice(), 0.0);
	}

	@Test
	public void checkIfProductDoesNotExistInProductDAO() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.getProduct(NON_EXISTING_NAME));
	}

	@Test
	public void checkIfCanAddSameProductTwice() {
		addTestingProduct();
		Assert.assertThrows(ProductDuplicateException.class,() -> productDAO.addProduct(EXISTING_NAME, irrelevantCategory, irrelevantPrice));
	}

	@Test
	public void checkIfCanRemoveExistingProductByName() throws ProductDoesNotExistException {
		addTestingProduct();
		productDAO.removeProduct(EXISTING_NAME);
		Assert.assertTrue(productDAO.getProducts().isEmpty());
	}

	@Test
	public void checkIfCanRemoveExistingProductByObject() throws ProductDoesNotExistException {
		addTestingProduct();
		productDAO.removeProduct(SimpleDAOTests.product);
		Assert.assertTrue(productDAO.getProducts().isEmpty());
	}
	
	@Test
	public void checkIfCanRemoveNonExistingProductByName() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.removeProduct(NON_EXISTING_NAME));
	}
	
	@Test
	public void checkIfCanRemoveNonExistingProductByObject() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.removeProduct(new Product(NON_EXISTING_NAME)));
	}

	@Test
	public void checkIfCanUpdateBasePriceInExistingProduct() throws ProductDoesNotExistException {
		addTestingProduct();
		productDAO.updateProductBasePrice(EXISTING_NAME, newBasePrice);
		Assert.assertEquals(newBasePrice, productDAO.getProduct(EXISTING_NAME).getBasePrice(), 0.0);
	}

	@Test
	public void checkIfCanUpdateBasePriceInNonExistingProduct() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.updateProductBasePrice(NON_EXISTING_NAME, newBasePrice));
	}

	@Test
	public void checkIfCanUpdateCategoryInExistingProduct() throws ProductDoesNotExistException {
		addTestingProduct();
		productDAO.updateProductCategory(EXISTING_NAME, newCategory);
		Assert.assertEquals(newCategory, productDAO.getProduct(EXISTING_NAME).getCategory());
	}

	@Test
	public void checkIfCanUpdateCategoryInNonExistingProduct() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.updateProductCategory(NON_EXISTING_NAME, newCategory));
	}

	private void addTestingProduct() {
		try {
			productDAO.addProduct(EXISTING_NAME, irrelevantCategory, irrelevantPrice);
		} catch (ProductDuplicateException e) {
			e.printStackTrace();
		}
	}

	//	@Test
//	public void checkIfStateNameExists() {
//		Mockito.when(stateDAO.getUSStateByName(EXISTING_NAME)).thenReturn(Optional.of(SimpleDAOTests.state));
//		Optional<USState> state = stateDAO.getUSStateByName(EXISTING_NAME);
//		Assert.assertEquals(SimpleDAOTests.state, state.orElse(null));
//	}
//
//	@Test
//	public void checkIfStateNameNotExists() {
//		Mockito.when(stateDAO.getUSStateByName(NAME_THAT_NOT_EXISTS)).thenReturn(Optional.empty());
//		Optional<USState> state = stateDAO.getUSStateByName(NAME_THAT_NOT_EXISTS);
//		Assert.assertNull(state.orElse(null));
//	}
//
//	@Test
//	public void checkIfStateNameIsNull() {
//		Mockito.when(stateDAO.getUSStateByName(null)).thenReturn(Optional.empty());
//		Optional<USState> state = stateDAO.getUSStateByName(null);
//		Assert.assertNull(state.orElse(null));
//	}
//
//	@Test
//	public void checkIfStateNameIsEmpty() {
//		Mockito.when(stateDAO.getUSStateByName("")).thenReturn(Optional.empty());
//		Optional<USState> state = stateDAO.getUSStateByName("");
//		Assert.assertNull(state.orElse(null));
//	}
//
//	@Test
//	public void checkIfProductNameExists() {
//		Optional<Product> product = simpleProductDAO.getProduct(EXISTING_NAME);
//		Assert.assertEquals(SimpleDAOTests.product, product.orElse(null));
//	}
//
//	@Test
//	public void checkIfProductNameNotExists() {
//		Optional<Product> product = simpleProductDAO.getProduct(NAME_THAT_NOT_EXISTS);
//		Assert.assertNull(product.orElse(null));
//	}
//
//	@Test
//	public void checkIfProductNameIsNull() {
//		Optional<Product> product = simpleProductDAO.getProduct(null);
//		Assert.assertNull(product.orElse(null));
//	}
//
//	@Test
//	public void checkIfProductNameIsEmpty() {
//		Optional<Product> product = simpleProductDAO.getProduct("");
//		Assert.assertNull(product.orElse(null));
//	}
}
