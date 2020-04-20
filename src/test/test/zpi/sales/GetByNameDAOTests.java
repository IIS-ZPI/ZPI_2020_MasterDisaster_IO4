package test.zpi.sales;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.dao.DAOFactory;
import zpi.product.Product;
import zpi.product.ProductDAO;
import zpi.state.IUSStateDAO;
import zpi.state.USState;
import zpi.state.SimpleUSStateDAO;

import java.util.Optional;

public class GetByNameDAOTests {
    private static final String NAME_THAT_EXISTS = "NameThatExistsInData";
    private static final String NAME_THAT_NOT_EXISTS = "NameThatNotExistsInData";

    private static IUSStateDAO stateDAO = new SimpleUSStateDAO();
    private static ProductDAO productDAO = ProductDAO.getInstance();

    private static USState state = new USState(NAME_THAT_EXISTS);
    private static Product product = new Product(NAME_THAT_EXISTS);

    @BeforeClass
    public static void init() {
        stateDAO.getStates().add(state);
        productDAO.getProducts().add(product);
    }

    @Test
    public void checkIfStateNameExists(){
        Optional<USState> state = stateDAO.getUSStateByName(NAME_THAT_EXISTS);
        Assert.assertEquals(GetByNameDAOTests.state, state.orElse(null));
    }

    @Test
    public void checkIfStateNameNotExists(){
        Optional<USState> state = stateDAO.getUSStateByName(NAME_THAT_NOT_EXISTS);
        Assert.assertNull(state.orElse(null));
    }

    @Test
    public void checkIfStateNameIsNull(){
        Optional<USState> state = stateDAO.getUSStateByName(null);
        Assert.assertNull(state.orElse(null));
    }

    @Test
    public void checkIfStateNameIsEmpty(){
        Optional<USState> state = stateDAO.getUSStateByName("");
        Assert.assertNull(state.orElse(null));
    }

    @Test
    public void checkIfProductNameExists(){
        Optional<Product> product = productDAO.getProductByName(NAME_THAT_EXISTS);
        Assert.assertEquals(GetByNameDAOTests.product, product.orElse(null));
    }

    @Test
    public void checkIfProductNameNotExists(){
        Optional<Product> product = productDAO.getProductByName(NAME_THAT_NOT_EXISTS);
        Assert.assertNull(product.orElse(null));
    }

    @Test
    public void checkIfProductNameIsNull(){
        Optional<Product> product = productDAO.getProductByName(null);
        Assert.assertNull(product.orElse(null));
    }

    @Test
    public void checkIfProductNameIsEmpty() {
        Optional<Product> product = productDAO.getProductByName("");
        Assert.assertNull(product.orElse(null));
    }
}
