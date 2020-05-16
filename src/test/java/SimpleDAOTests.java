package java.zpi.sales;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zpi.category.Category;
import zpi.product.*;
import zpi.state.IUSStateDAO;
import zpi.state.SimpleUSStateDAO;
import zpi.state.USState;

import java.util.Optional;

public class SimpleDAOTests {
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
		stateDAO.getStates().clear();
	}

	private void addTestingProduct() {
		try {
			productDAO.addProduct(EXISTING_NAME, irrelevantCategory, irrelevantPrice);
		} catch (ProductDuplicateException e) {
			e.printStackTrace();
		}
	}

	private void addTestingState(){
		stateDAO.addUSState(EXISTING_NAME);
	}

	// --- PRODUCT TESTS --- //


	@Test
	public void checkIfProductExistsInProductDAO() throws ProductDoesNotExistException {
		addTestingProduct();
		Product product = productDAO.getProduct(EXISTING_NAME);
		Assert.assertEquals(SimpleDAOTests.product.getName(), product.getName());
		Assert.assertEquals(SimpleDAOTests.product.getCategory(), product.getCategory());
		Assert.assertEquals(SimpleDAOTests.product.getBasePrice(), product.getBasePrice(), 0.0);
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


	// --- STATE TESTS --- //


	@Test
	public void checkIfStateNameExists() {
		addTestingState();
		Optional<USState> state = stateDAO.getUSStateByName(EXISTING_NAME);
		Assert.assertEquals(SimpleDAOTests.state.getName(), state.orElse(null).getName());
	}

	@Test
	public void checkIfStateNameDoesNotExist() {
		addTestingState();
		Optional<USState> state = stateDAO.getUSStateByName(NON_EXISTING_NAME);
		Assert.assertNull(state.orElse(null));
	}

	@Test
	public void checkIfStateNameIsNull() {
		addTestingState();
		Assert.assertThrows(NullPointerException.class, () -> stateDAO.getUSStateByName(null));
	}

	@Test
	public void checkIfStateNameIsEmpty() {
		addTestingState();
		Optional<USState> state = stateDAO.getUSStateByName("");
		Assert.assertNull(state.orElse(null));
	}

	@Test
	public void checkIfCanEditStateTax() {
		double taxRatio = 0.23;
		stateDAO.editCategoryTax(state, irrelevantCategory, taxRatio);
		Assert.assertEquals(taxRatio, state.getTaxForCategory(irrelevantCategory), 0.0);
	}
}
