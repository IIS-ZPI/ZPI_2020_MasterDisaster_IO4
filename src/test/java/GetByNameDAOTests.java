import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.product.Product;
import zpi.product.ProductDAO;
import zpi.sales.Category;
import zpi.sales.CategoryDAO;
import zpi.state.USState;
import zpi.state.USStateDAO;

import java.util.Optional;

public class GetByNameDAOTests {
    private static final String NAME_THAT_EXISTS = "NameThatExistsInData";
    private static final String NAME_THAT_NOT_EXISTS = "NameThatNotExistsInData";

    private static USStateDAO stateDAO = USStateDAO.getInstance();
    private static ProductDAO productDAO = ProductDAO.getInstance();
    private static CategoryDAO categoryDAO = CategoryDAO.getInstance();

    private static USState state = new USState(NAME_THAT_EXISTS);
    private static Product product = new Product(NAME_THAT_EXISTS);
    private static Category category = new Category(NAME_THAT_EXISTS);

    @BeforeClass
    public static void init() {
        stateDAO.getStates().add(state);
        productDAO.getProducts().add(product);
        categoryDAO.getCategories().add(category);
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

    @Test
    public void checkIfCategoryNameExists(){
        Optional<Category> category = categoryDAO.getCategoryByName(NAME_THAT_EXISTS);
        Assert.assertEquals(GetByNameDAOTests.category, category.orElse(null));
    }

    @Test
    public void checkIfCategoryNameNotExists(){
        Optional<Category> category = categoryDAO.getCategoryByName(NAME_THAT_NOT_EXISTS);
        Assert.assertNull(category.orElse(null));
    }

    @Test
    public void checkIfCategoryNameIsNull(){
        Optional<Category> category = categoryDAO.getCategoryByName(null);
        Assert.assertNull(category.orElse(null));
    }

    @Test
    public void checkIfCategoryNameIsEmpty() {
        Optional<Category> category = categoryDAO.getCategoryByName("");
        Assert.assertNull(category.orElse(null));
    }
}
