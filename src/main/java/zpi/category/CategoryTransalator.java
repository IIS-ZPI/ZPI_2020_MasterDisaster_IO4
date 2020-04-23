package zpi.category;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryTransalator {
	public static String tr(Category category) {
		switch (category) {
			case GROCERIES:
				return "Groceries";
			case PREPARED_FOOD:
				return "Prepared food";
			case PRESCRIPTION_DRUG:
				return "Prescription drug";
			case NON_PRESCRIPTION_DRUG:
				return "Non-prescription drug";
			case CLOTHING:
				return "Clothing";
			case INTANGIBLES:
				return "Intangibles";
		}
		return "";
	}
	
	static public List<String> getPrettyNamesOfCategories() {
		return Arrays.stream(Category.values())
				.map(CategoryTransalator::tr)
				.collect(Collectors.toList());
	}
}
