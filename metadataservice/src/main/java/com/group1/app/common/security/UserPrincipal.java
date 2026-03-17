package com.group1.app.common.security;

public class UserPrincipal {

    private final String userId;
    private final String name;

    public UserPrincipal(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}