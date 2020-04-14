package zpi.category;

public class CategoryTransalator {
	private CategoryTransalator() {
	}
	
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
}
