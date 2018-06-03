package com.filichkin.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Receiver extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        EventBus eb = vertx.eventBus();

        eb.consumer("ping-address", message -> {

            System.out.println("Received message: " + message.body());
            // Now send back reply
            message.reply("pong!");
        });

        System.out.println("Receiver ready!");
    }
}
