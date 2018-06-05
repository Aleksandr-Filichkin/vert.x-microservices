package com.filichkin.vertx.api.gateway;

import com.filichkin.vertx.verticle.BaseVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceReference;

import java.util.function.Consumer;

public class ApiGatewayApp extends BaseVerticle {

    public static final String SERVICE_PREFIX = "/service/";
    private static Logger LOGGER = LoggerFactory.getLogger(ApiGatewayApp.class);
    private ServiceRegistrationHolder serviceRegistrationHolder = new ServiceRegistrationHolder();

@Override
    public int getPort() {
        return 8080;
    }

    @Override
    protected Router buildRouter() {
        Router router = super.buildRouter();
        router.get(SERVICE_PREFIX + "*").handler(this::dispatchRequests);
        router.post(SERVICE_PREFIX + "*").handler(this::dispatchRequests);
        return router;
    }

    private void discoverAndRegisterService(String serviceName, RoutingContext context) {
        LOGGER.info("try to discover service {0}", serviceName);
        getDiscovery().getRecord(new JsonObject().put("name", "service-" + serviceName), ar -> {
            if (ar.succeeded() && ar.result() != null) {
                // Retrieve the service reference
                ServiceReference reference = getDiscovery().getReference(ar.result());
                WebClient webClient = reference.getAs(WebClient.class);
                serviceRegistrationHolder.registerService(serviceName, webClient);
                proxyRequest(context).accept(webClient);
            } else {

                Exception exception = new ServiceDiscoveryException(String.format("Cannot discover service '%s'. Looks like service was not resistered", serviceName));
                LOGGER.error(exception);
                context.fail(exception);
            }
        });
    }

    private void dispatchRequests(RoutingContext context) {

        String uri = context.request().uri();
        String serviceName = Util.getServiceFromUri(uri);

        if (!serviceRegistrationHolder.exist(serviceName)) {
            discoverAndRegisterService(serviceName, context);
        } else {
            proxyRequest(context).accept(serviceRegistrationHolder.getServiceWebClient(serviceName));
        }

    }


    private Consumer<WebClient> proxyRequest(RoutingContext context) {
        return webClient -> webClient.get("/api/" + context.request().uri().substring(SERVICE_PREFIX.length()))
                .send(asyncResult -> {
                    LOGGER.info("Proxying response: " + asyncResult.result().statusCode());
                    context.response().setChunked(true);
                    context.response().setStatusCode(asyncResult.result().statusCode());
                    context.response().headers().setAll(asyncResult.result().headers());
                    context.response().end(asyncResult.result().bodyAsBuffer());

                });
    }


    @Override
    protected String getServiceName() {
        return "api-gateway";
    }
}
