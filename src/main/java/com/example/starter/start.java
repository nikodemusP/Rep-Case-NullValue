package com.example.starter;

import io.vertx.core.Vertx;

public class start {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }
}
