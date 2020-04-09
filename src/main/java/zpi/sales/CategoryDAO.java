package zpi.sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    private final List<Category> categories = new ArrayList<>();
    private final static CategoryDAO INSTANCE = new CategoryDAO();

    private CategoryDAO(){
        prepareTempData();
    }

    public static CategoryDAO getInstance(){
        return INSTANCE;
    }

    void prepareTempData() {
        categories.add(new Category("Groceries"));
        categories.add(new Category("Prepared food"));
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Optional<Category> getCategoryByName(String name){
        return categories.stream()
                .filter(e -> e.getName().equals(name))
                .findAny();
    }
}
