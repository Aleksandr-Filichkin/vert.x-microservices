package com.filichkin.vertx.api.gateway;

import io.vertx.ext.web.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistrationHolder {
    private Map<String, WebClient> registeredServicesMap = new ConcurrentHashMap<>();

    public void registerService(String serviceName, WebClient webClient) {
        registeredServicesMap.put(serviceName, webClient);
    }

    public boolean exist(String serviceName) {
        return registeredServicesMap.containsKey(serviceName);
    }

    public WebClient getServiceWebClient(String service) {
        return registeredServicesMap.get(service);
    }
}
