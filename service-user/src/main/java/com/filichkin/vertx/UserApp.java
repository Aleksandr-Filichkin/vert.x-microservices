package com.filichkin.vertx;

import com.filichkin.vertx.verticle.BaseVerticle;
import io.vertx.core.eventbus.EventBus;

public class UserApp extends BaseVerticle {


    @Override
    public void start() throws Exception {

        EventBus eb = vertx.eventBus();

        eb.consumer("proxy-user", message -> {

            message.reply("hello from user " + message.body());
        });
        // Now send back reply


        System.out.println("Receiver ready!");
    }
}
