package com.filichkin.vertx.example;

import com.filichkin.vertx.Receiver;
import com.filichkin.vertx.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

public class ConsumerApp {


    public static void main(String[] args) throws InterruptedException {
        // Create an HTTP server which simply returns "Hello World!" to each request.

        Vertx vertx= Vertx.vertx();
        Router router = Router.router(vertx);
        UserService userService = new UserService();
        router.get("/api/user/:id")
                .handler(routingContext -> {
                    String id = routingContext.request()
                            .getParam("id");
                    routingContext.response()
                            .putHeader("content-type", "application/json")
                            .setStatusCode(200)
                            .end(Json.encodePrettily(userService.getUser(id)));
                });
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

        vertx.deployVerticle(new Receiver());


    }

}
