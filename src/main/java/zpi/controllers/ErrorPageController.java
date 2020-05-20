package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.product.IProductDAO;
import zpi.product.SimpleProductDAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class ErrorPageController {
    public static Handler error404 = ctx -> {
        IProductDAO simpleProductDao = DAOFactory.getIProductDAO();

        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("title", "404 error");


        ctx.render(Paths.Template.PAGE_NOT_FOUND, model);
    };
}
