import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import zpi.product.Product;
import zpi.product.ProductDAO;
import zpi.state.IUSStateDAO;
import zpi.state.USState;


import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class GetByNameDAOTests {
	private static final String NAME_THAT_EXISTS = "NameThatExistsInData";
	private static final String NAME_THAT_NOT_EXISTS = "NameThatNotExistsInData";
	
	@Mock
	private static IUSStateDAO stateDAO = Mockito.mock(IUSStateDAO.class);
	private static ProductDAO productDAO = ProductDAO.getInstance();
	
	private static USState state = new USState(NAME_THAT_EXISTS);
	private static Product product = new Product(NAME_THAT_EXISTS);
	
	@BeforeClass
	public static void init() {
		stateDAO.getStates().add(state);
		productDAO.getProducts().add(product);
	}
	
	@Test
	public void checkIfStateNameExists() {
		Mockito.when(stateDAO.getUSStateByName(NAME_THAT_EXISTS)).thenReturn(Optional.of(GetByNameDAOTests.state));
		Optional<USState> state = stateDAO.getUSStateByName(NAME_THAT_EXISTS);
		Assert.assertEquals(GetByNameDAOTests.state, state.orElse(null));
	}
	
	@Test
	public void checkIfStateNameNotExists() {
		Mockito.when(stateDAO.getUSStateByName(NAME_THAT_NOT_EXISTS)).thenReturn(Optional.empty());
		Optional<USState> state = stateDAO.getUSStateByName(NAME_THAT_NOT_EXISTS);
		Assert.assertNull(state.orElse(null));
	}
	
	@Test
	public void checkIfStateNameIsNull() {
		Mockito.when(stateDAO.getUSStateByName(null)).thenReturn(Optional.empty());
		Optional<USState> state = stateDAO.getUSStateByName(null);
		Assert.assertNull(state.orElse(null));
	}
	
	@Test
	public void checkIfStateNameIsEmpty() {
		Mockito.when(stateDAO.getUSStateByName("")).thenReturn(Optional.empty());
		Optional<USState> state = stateDAO.getUSStateByName("");
		Assert.assertNull(state.orElse(null));
	}
	
	@Test
	public void checkIfProductNameExists() {
		Optional<Product> product = productDAO.getProductByName(NAME_THAT_EXISTS);
		Assert.assertEquals(GetByNameDAOTests.product, product.orElse(null));
	}
	
	@Test
	public void checkIfProductNameNotExists() {
		Optional<Product> product = productDAO.getProductByName(NAME_THAT_NOT_EXISTS);
		Assert.assertNull(product.orElse(null));
	}
	
	@Test
	public void checkIfProductNameIsNull() {
		Optional<Product> product = productDAO.getProductByName(null);
		Assert.assertNull(product.orElse(null));
	}
	
	@Test
	public void checkIfProductNameIsEmpty() {
		Optional<Product> product = productDAO.getProductByName("");
		Assert.assertNull(product.orElse(null));
	}
}
