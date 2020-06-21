package com.route.core;

public interface RouteFileListener {
    void handleEntry(String origin, String destination, int cost);
}
