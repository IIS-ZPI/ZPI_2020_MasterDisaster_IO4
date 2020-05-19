package zpi.utils;

import io.javalin.http.Context;

public class RequestUtil {
	static public String getStateName(Context ctx) {
		return ctx.pathParam("name");
	}
	
	public static String getProductName(Context ctx) {
		return ctx.formParam("editProductName");
	}
	public static String getProductCategory(Context ctx) {
		return ctx.formParam("editCategoryName");
	}
	public static Double getProductBasePrice(Context ctx) {
		return Double.valueOf(ctx.formParam("editBasePrice"));
	}
}
