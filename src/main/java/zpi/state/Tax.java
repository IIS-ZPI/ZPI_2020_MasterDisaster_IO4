package zpi.state;

public class Tax {
	double baseTax = 0.;
	//Above this value tax will be added.
	double valueWithoutTax = 0.;
	
	public Tax() {
	}
	
	public Tax(double baseTax) {
		this.baseTax = baseTax;
	}
	
	public Tax(double baseTax, double valueWithoutTax) {
		this.baseTax = baseTax;
		this.valueWithoutTax = valueWithoutTax;
	}
	
	public double getBaseTax() {
		return baseTax;
	}
	
	public double getValueWithoutTax() {
		return valueWithoutTax;
	}
}

