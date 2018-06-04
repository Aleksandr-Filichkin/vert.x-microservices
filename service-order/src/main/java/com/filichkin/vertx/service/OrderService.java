package com.filichkin.vertx.service;

import com.filichkin.vertx.model.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {

    private Map<Long,Order> orderMap= new ConcurrentHashMap<>();

    public Order getOrder(long id){
        return orderMap.get(id);
    }
    public void addOrder(Order order){
         orderMap.put(order.getId(),order);
    }

}
