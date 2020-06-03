package zpi.utils;

import io.javalin.http.Context;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestUtil {
	static public String getStateName(Context ctx) {
		return ctx.pathParam("name");
	}
	
	public static String getProductName(Context ctx) {
		return ctx.formParam("productName");
	}
	
	public static Map<String, String> parseJSONStandardArrayToMap(Context ctx){
		Map rV = new HashMap<String, String>();
		var bodyArgs = new JSONArray(ctx.body());
		
		for(int i = 0; i < bodyArgs.length(); ++i){
			var arg = bodyArgs.getJSONObject(i);
			
			rV.put(arg.get("name"), arg.getString("value"));
		}
		
		return rV;
	}
	
	
	/**
	 * @param params Map, usually get from parseJSONStandardArrayToMap
	 * @param args Key, that map have to contains
	 * @return True, if all given keys are in map.
	 */
	public static boolean isMapContainsRequiredParams(Map params, String ... args){
		for(var arg : args){
			if(!params.containsKey(arg)) return false;
		}
		return true;
	}
}
