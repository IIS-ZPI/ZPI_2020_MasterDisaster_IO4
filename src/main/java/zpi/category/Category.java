package zpi.category;

public enum Category {
	GROCERIES(1),
	PREPARED_FOOD(2),
	PRESCRIPTION_DRUG(3),
	NON_PRESCRIPTION_DRUG(4),
	CLOTHING(5),
	INTANGIBLES(6),
	;
	
	//This the category ID in database
	private int value;
	
	Category(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
