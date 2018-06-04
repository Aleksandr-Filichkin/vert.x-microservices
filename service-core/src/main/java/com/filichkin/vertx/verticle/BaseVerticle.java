package com.filichkin.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BaseVerticle extends AbstractVerticle {

    protected Router buildRouter(){
        Router router = Router.router(vertx);
        // body handler
        router.route().handler(BodyHandler.create());
        router.get("/status").handler(routingContext -> routingContext.response().putHeader("content-type", "application/json").end("OK"));
        return router;
    }

    @Override
    public void start() throws Exception{
        vertx.createHttpServer().requestHandler(buildRouter()::accept).listen(8080);
    }
}
