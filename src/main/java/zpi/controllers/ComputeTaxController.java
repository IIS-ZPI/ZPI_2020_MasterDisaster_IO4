package zpi.controllers;

import io.javalin.http.Handler;

public class ComputeTaxController {
	
	public static Handler computeTax = ctx ->{
		ctx.html("Result = ");
	};
}
