package zpi.controllers;

import io.javalin.http.Handler;
import zpi.dao.DAOFactory;
import zpi.product.IProductDAO;
import zpi.utils.Paths;
import zpi.utils.ViewUtil;

import java.util.Map;

public class AboutPaigeController {
    public static Handler aboutPage = ctx -> {
        IProductDAO simpleProductDao = DAOFactory.getIProductDAO();

        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("title", "About");

        ctx.render(Paths.Template.ABOUT, model);
    };
}
