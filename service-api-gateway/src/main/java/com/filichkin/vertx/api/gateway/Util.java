package com.filichkin.vertx.api.gateway;

public class Util {
    public static String getServiceFromUri(String uri) {
        int initialOffset = "/service/".length(); // length of `/service/`
        return uri.substring(initialOffset, uri.indexOf('/', initialOffset));

    }
}
