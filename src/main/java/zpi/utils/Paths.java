package zpi.utils;

public class Paths {
	public static class Web {
		public static final String INDEX = "";
		public static final String SIMPLE_TAX = "/compute-tax";
		public static final String ALL_STATES = "/states";
		public static final String ALL_PRODUCTS = "/products";
		public static final String SINGLE_STATE = "/state/:name";
		public static final String ABOUT = "/about";
	}
	
	public static class Template {
		public static final String INDEX = "/velocity/index/index.vm";
		public static final String ALL_STATES = "/velocity/states/states.vm";
		public static final String ALL_PRODUCTS = "/velocity/products/products.vm";
		public static final String SINGLE_STATE = "/velocity/states/single_state.vm";
		public static final String SINGLE_RESULT = "/velocity/taxes/single_result.vm";
		public static final String PAGE_NOT_FOUND = "/velocity/errors/pageNotFound.vm";
		public static final String ABOUT = "/velocity/about/about.vm";
	}
}
