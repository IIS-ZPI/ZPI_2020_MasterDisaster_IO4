package zpi.utils;

import io.javalin.http.Context;

public class RequestUtil {
	static public String getStateName(Context ctx) {
		return ctx.pathParam("name");
	}
}
