import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.category.Category;
import zpi.db.Database;
import zpi.product.MSSQLProductDAO;
import zpi.product.Product;
import zpi.product.ProductDoesNotExistException;
import zpi.product.ProductDuplicateException;
import zpi.state.MSSQUSStateDAO;
import zpi.state.USState;

import java.util.Optional;


public class DatabaseTest {
	private static final String[] passToDatabase = new String[]{"--HOSTNAME", "masterdisaster.database.windows.net",
																"--DBNAME", "masterdisaster-db",
																"--DBUSER", "masterdisaster",
																"--DBPASSWORD", "ZPI2020ksakiwjm"};
	private static Database database = new Database();
	private static MSSQLProductDAO productDAO;
	private static MSSQUSStateDAO stateDAO;

	private static Product product;
	private static USState existingState;

	private static final String EXISTING_NAME = "TestingProduct";
	private static final String EXISTING_STATE_NAME = "Alabama";
	private static final String NON_EXISTING_NAME = "NonExistingProduct";
	private static final Category IRRELEVANT_CATEGORY = Category.GROCERIES;
	private static final double IRRELEVANT_PRICE = 1.0;

	private static Product addTestingProduct(String name) {
		Product product = null;
		try {
			product = productDAO.addProduct(name, IRRELEVANT_CATEGORY, IRRELEVANT_PRICE);
		} catch (ProductDuplicateException e) {
			e.printStackTrace();
		}
		return product;
	}

	@BeforeClass
	public static void init(){
		database.createDBConnectionFromCommandLine(passToDatabase);
		database.initializeTables();

		productDAO = new MSSQLProductDAO(database);
		stateDAO = new MSSQUSStateDAO(database);

		existingState = new USState(EXISTING_STATE_NAME);
//		product = addTestingProduct(EXISTING_NAME);
	}

	// --- PRODUCT TESTS --- //

	@Test
	public void shouldReturnExistingProductIfGettingProduct() throws Exception {
		Product product = productDAO.getProduct(EXISTING_NAME);
		Assert.assertEquals(DatabaseTest.product.getName(), product.getName());
	}

	@Test
	public void shouldThrowProductDoesNotExistExceptionIfGettingProduct() {
		Assert.assertThrows(ProductDoesNotExistException.class,
				() -> productDAO.getProduct(NON_EXISTING_NAME));
	}

	@Test
	public void shouldThrowProductDuplicateExceptionIfAddingProduct() {
		Assert.assertThrows(ProductDuplicateException.class,
				() -> productDAO.addProduct(EXISTING_NAME, IRRELEVANT_CATEGORY, IRRELEVANT_PRICE));
	}

//	@Test
//	public void shouldReturnNewProductIfAddingProduct() throws Exception{
//		String localName = "TestingProduct2";
//		var newProduct = addTestingProduct(localName);
//
//		Assert.assertEquals(localName, newProduct.getName());
//		Assert.assertEquals(IRRELEVANT_CATEGORY, newProduct.getCategory());
//		Assert.assertEquals(IRRELEVANT_PRICE, newProduct.getBasePrice(), 0.0);
//		Assert.assertTrue(productDAO.getProducts().contains(newProduct));
//	}

	@Test
	public void shouldChangeExistingProductBasePriceIfUpdatingProduct() throws Exception {
		double newBasePrice = 2.0;
		productDAO.updateProductBasePrice(EXISTING_NAME, newBasePrice);
	}

	@Test
	public void shouldChangeExistingProductCategoryIfUpdatingProduct() throws Exception {
		Category newIrrelevantCategory = Category.CLOTHING;
		productDAO.updateProductCategory(EXISTING_NAME, newIrrelevantCategory);
	}

	@Test
	public void shouldThrowProductDoesNotExistExceptionIfUpdatingProduct() {
		Assert.assertThrows(ProductDoesNotExistException.class,
				() ->productDAO.updateProductBasePrice(NON_EXISTING_NAME, IRRELEVANT_PRICE));
	}

	@Test
	public void shouldThrowProductDoesNotExistExceptionIfRemovingProducc() {
		Assert.assertThrows(ProductDoesNotExistException.class,
				() -> productDAO.removeProduct(NON_EXISTING_NAME));
	}

//	@Test
//	public void shouldRemoveExistingProduct() throws Exception {
//		String localName = "TestingProduct3";
//		Product newProduct = addTestingProduct(localName);
//		productDAO.removeProduct(newProduct);
//		Assert.assertFalse(productDAO.getProducts().contains(newProduct));
//	}

	// --- STATE TESTS --- //

	@Test
	public void shouldReturnExistingState() {
		Optional<USState> state = stateDAO.getUSStateByName(EXISTING_STATE_NAME);
		Assert.assertTrue(state.isPresent());
		Assert.assertEquals(DatabaseTest.existingState.getName(), state.get().getName());
	}

	@Test
	public void shouldReturnEmptyOptionalIfGettingNonExistingState() {
		Optional<USState> state = stateDAO.getUSStateByName(NON_EXISTING_NAME);
		Assert.assertTrue(state.isEmpty());
	}

	@Test
	public void shouldReturnEmptyOptionalIfGettingNullState() {
		Optional<USState> state = stateDAO.getUSStateByName(null);
		Assert.assertTrue(state.isEmpty());
	}

	@Test
	public void shouldReturnEmptyOptionalIfGettingEmptyState() {
		Optional<USState> state = stateDAO.getUSStateByName("");
		Assert.assertTrue(state.isEmpty());
	}

	@Test
	public void shouldEditExistingStateWithNewBaseTax() {
		double newTaxValue = 0.23;
		stateDAO.editCategoryBaseTax(existingState, IRRELEVANT_CATEGORY, newTaxValue);
		existingState = stateDAO.getUSStateByName(existingState.getName()).get();
		Assert.assertEquals(newTaxValue, existingState.getTaxForCategory(IRRELEVANT_CATEGORY).getBaseTax(), 0.0);
	}

	@Test
	public void shouldEditExistingStateWithNewValueWithoutTax() {
		double newValueWithoutTax = 15.5;
		stateDAO.editCategoryValueWithoutTax(existingState, IRRELEVANT_CATEGORY, newValueWithoutTax);
		existingState = stateDAO.getUSStateByName(existingState.getName()).get();
		Assert.assertEquals(newValueWithoutTax, existingState.getTaxForCategory(IRRELEVANT_CATEGORY).getValueWithoutTax(), 0.0);
	}
}
