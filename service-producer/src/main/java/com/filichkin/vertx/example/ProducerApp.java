package com.filichkin.vertx.example;

import com.filichkin.vertx.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ProducerApp extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        EventBus eb = vertx.eventBus();
//

        vertx.setPeriodic(1000, v -> {

            eb.send("ping-address", "ping!", reply -> {
                if (reply.succeeded()) {
                    System.out.println("Received reply " + reply.result().body());
                } else {
                    System.out.println("No reply");
                }
            });

        });
    }


}
