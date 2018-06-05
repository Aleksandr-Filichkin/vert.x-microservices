package com.filichkin.vertx;

import com.filichkin.vertx.handler.HttpRequestHandler;
import com.filichkin.vertx.service.OrderService;
import com.filichkin.vertx.verticle.BaseVerticle;
import io.vertx.ext.web.Router;

public class OrderApp extends BaseVerticle {

    private HttpRequestHandler httpRequestHandler = new HttpRequestHandler(new OrderService());

    @Override
    protected String getServiceName() {
        return "order";
    }


    @Override
    protected Router buildRouter() {
        Router router = super.buildRouter();

        router.get("/api/order/:id").handler(httpRequestHandler::getOrderById);
        router.post("/api/order").handler(httpRequestHandler::addOrder);
        return router;
    }


}
