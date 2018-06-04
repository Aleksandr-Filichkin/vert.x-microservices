package com.filichkin.vertx.api.gateway;

public class Util {
    public static String getServiceFromUri(String uri){
        int initialOffset = 5; // length of `/api/`
        return uri.substring(initialOffset,uri.indexOf('/',5));

    }
}
