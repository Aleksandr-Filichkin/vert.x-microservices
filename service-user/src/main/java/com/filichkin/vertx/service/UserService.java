package com.filichkin.vertx.service;

import com.filichkin.vertx.model.User;

public class UserService {

    public User getUser(String name) {
        User user = new User();
        user.setFirstName(name);
        return user;
    }
}
