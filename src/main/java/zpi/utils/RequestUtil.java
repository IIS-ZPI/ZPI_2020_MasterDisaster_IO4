package zpi.utils;

import io.javalin.http.Context;

import java.util.Objects;

public class RequestUtil {
	static public String getStateName(Context ctx) {
		return ctx.pathParam("name");
	}
	
	public static String getProductName(Context ctx) {
		return ctx.formParam("productName");
	}
	public static String getProductCategory(Context ctx) {
		return ctx.formParam("categoryName");
	}
	public static Double getProductBasePrice(Context ctx) {
		return Double.valueOf(ctx.formParam("basePrice"));
	}
}
