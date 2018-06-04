package com.filichkin.vertx.api.gateway;

import com.filichkin.vertx.verticle.BaseVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ApiGatewayApp extends BaseVerticle {

    @Override
    protected Router buildRouter() {
        Router router= super.buildRouter();
        router.route("/api/*").handler(this::dispatchRequests);
        return router;
    }

    @Override
    public void start() throws Exception {
        super.start();
    }
    private void dispatchRequests(RoutingContext context) {

        String uri=context.request().uri();
        String serviceName=Util.getServiceFromUri(uri);

        EventBus eb = vertx.eventBus();
        eb.send("proxy-"+serviceName, context.request().getParam("id"),  reply -> {
            if (reply.succeeded()) {
                context.response().setStatusCode(200).end(String.valueOf(reply.result().body()));
            } else {
                context.response().setStatusCode(500).end("Error");
            }
        });
    }
}
