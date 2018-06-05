package com.filichkin.vertx;

import com.filichkin.vertx.handler.HttpRequestHandler;
import com.filichkin.vertx.service.OrderService;
import com.filichkin.vertx.verticle.BaseVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class OrderApp extends BaseVerticle {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderApp.class);
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

    @Override
    public void start() throws Exception {

        // Record creation from a type
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
        Record record = HttpEndpoint.createRecord("service-" + getServiceName(), "localhost", getPort(), "/api");
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                // publication succeeded
                LOGGER.info("service {0} was registered", getServiceName());
            } else {
                // publication failed
            }
        });


    }


}
