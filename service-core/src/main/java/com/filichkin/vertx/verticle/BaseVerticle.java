package com.filichkin.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class BaseVerticle extends AbstractVerticle {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseVerticle.class);
    private int httpServerPort;
    private ServiceDiscovery discovery;

    protected abstract String getServiceName();

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

    }

    protected Router buildRouter() {
        Router router = Router.router(vertx);
        // body handler
        router.route().handler(BodyHandler.create());
        router.route().failureHandler(ctx -> ctx.response().setStatusCode(500).putHeader("content-type", "application/json").end(Json.encodePrettily(ctx.failure().getMessage())));
        router.get("/status").handler(routingContext -> routingContext.response().putHeader("content-type", "application/json").end("OK"));
        return router;
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer().requestHandler(buildRouter()::accept).listen(getPort());
        httpServerPort = httpServer.actualPort();
        LOGGER.info("Service {0} was started on port {1}", getServiceName(), getHttpServerPort());
        discovery = ServiceDiscovery.create(vertx);
        registerService();
    }

    private void registerService() {
        Record record = HttpEndpoint.createRecord("service-" + getServiceName(), "localhost", httpServerPort, "/api/" + getServiceName());
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                // publication succeeded
                ar.result();
                LOGGER.info("service {0} was registered", getServiceName());
            } else {
                LOGGER.error("cannot register service " + getServiceName());
            }
        });
    }

    protected int getPort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }

    public ServiceDiscovery getDiscovery() {
        return discovery;
    }

    public void setDiscovery(ServiceDiscovery discovery) {
        this.discovery = discovery;
    }

    public int getHttpServerPort() {
        return httpServerPort;
    }

    public void setHttpServerPort(int httpServerPort) {
        this.httpServerPort = httpServerPort;
    }
}
