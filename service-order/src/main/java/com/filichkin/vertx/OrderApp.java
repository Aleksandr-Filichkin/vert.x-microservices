package com.filichkin.vertx;

import com.filichkin.vertx.handler.HttpRequestHandler;
import com.filichkin.vertx.service.OrderService;
import com.filichkin.vertx.verticle.BaseVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;

public class OrderApp extends BaseVerticle {

    private HttpRequestHandler httpRequestHandler = new HttpRequestHandler(new OrderService());

    @Override
    protected Router buildRouter() {
        Router router = super.buildRouter();
        router.get("/api/order/:id").handler(httpRequestHandler::getOrderById);
        router.post("/api/order").handler(httpRequestHandler::addOrder);
        return router;
    }

    @Override
    public void start() throws Exception {


        super.start();
        EventBus eb = vertx.eventBus();
        vertx.setPeriodic(11000, v -> {
            DeliveryOptions deliveryOptions = new DeliveryOptions().setSendTimeout(10000);
            eb.send("ping-address", "ping!", deliveryOptions, reply -> {
                if (reply.succeeded()) {
                    System.out.println("Received reply " + reply.result().body());
                } else {
                    System.out.println("No reply");
                }
            });
        });
    }


}
