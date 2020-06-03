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
	private static final Category IRRELEVANT_CATEGORY = Category.GROCERIES;
	private static final Category NEW_CATEGORY = Category.CLOTHING;
	private static final double IRRELEVANT_PRICE = 1.0;
	private static final double NEW_BASE_PRICE = 2.0;

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
		product.setCategory(IRRELEVANT_CATEGORY);
		product.setBasePrice(IRRELEVANT_PRICE);
	}

	@Before
	public void clean(){
		productDAO.getProducts().clear();
		stateDAO.getStates().clear();
	}

	private void addTestingProduct() {
		try {
			productDAO.addProduct(EXISTING_NAME, IRRELEVANT_CATEGORY, IRRELEVANT_PRICE);
		} catch (ProductDuplicateException e) {
			e.printStackTrace();
		}
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
		Assert.assertThrows(ProductDuplicateException.class,() -> productDAO.addProduct(EXISTING_NAME, IRRELEVANT_CATEGORY, IRRELEVANT_PRICE));
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
		productDAO.updateProductBasePrice(EXISTING_NAME, NEW_BASE_PRICE);
		Assert.assertEquals(NEW_BASE_PRICE, productDAO.getProduct(EXISTING_NAME).getBasePrice(), 0.0);
	}

	@Test
	public void checkIfCanUpdateBasePriceInNonExistingProduct() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.updateProductBasePrice(NON_EXISTING_NAME, NEW_BASE_PRICE));
	}

	@Test
	public void checkIfCanUpdateCategoryInExistingProduct() throws ProductDoesNotExistException {
		addTestingProduct();
		productDAO.updateProductCategory(EXISTING_NAME, NEW_CATEGORY);
		Assert.assertEquals(NEW_CATEGORY, productDAO.getProduct(EXISTING_NAME).getCategory());
	}

	@Test
	public void checkIfCanUpdateCategoryInNonExistingProduct() {
		addTestingProduct();
		Assert.assertThrows(ProductDoesNotExistException.class, () -> productDAO.updateProductCategory(NON_EXISTING_NAME, NEW_CATEGORY));
	}


	// --- STATE TESTS --- //
	@Test
	public void checkIfStateNameExists() {
		Optional<USState> state = stateDAO.getUSStateByName(EXISTING_NAME);
		Assert.assertEquals(SimpleDAOTests.state.getName(), state.orElse(null).getName());
	}

	@Test
	public void checkIfStateNameDoesNotExist() {
		Optional<USState> state = stateDAO.getUSStateByName(NON_EXISTING_NAME);
		Assert.assertNull(state.orElse(null));
	}

	@Test
	public void checkIfStateNameIsNull() {
		Assert.assertThrows(NullPointerException.class, () -> stateDAO.getUSStateByName(null));
	}

	@Test
	public void checkIfStateNameIsEmpty() {
		Optional<USState> state = stateDAO.getUSStateByName("");
		Assert.assertNull(state.orElse(null));
	}
}
