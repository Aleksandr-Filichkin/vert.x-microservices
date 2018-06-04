package com.filichkin.vertx.handler;

import com.filichkin.vertx.model.Order;
import com.filichkin.vertx.service.OrderService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class HttpRequestHandler {
    private final OrderService orderService;

    public HttpRequestHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    public void getOrderById(RoutingContext routingContext) {
        Order order = orderService.getOrder(Long.parseLong(routingContext.request().getParam("id")));
        if (order != null) {
            routingContext.response().putHeader("content-type", "application/json").end(Json.encodePrettily(order));
        } else {
            routingContext.response().setStatusCode(404).putHeader("content-type", "application/json").end(Json.encodePrettily("Order doesn't exist"));
        }

    }

    public void addOrder(RoutingContext routingContext) {
        Order order = Json.decodeValue(routingContext.getBodyAsString(), Order.class);
        orderService.addOrder(order);
        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(order));
    }
}
