package com.route.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Route {
    private final List<String> path;
    private int cost;

    public Route(String origin) {
        path = new ArrayList<>();
        path.add(origin);
        cost = 0;
    }

    public Route(Route that) {
        this.path = new ArrayList<>(that.path);
        this.cost = that.cost;
    }

    public void add(Map.Entry<String, Integer> entry) {
        path.add(entry.getKey());
        cost += entry.getValue();
    }

    public int getCost() {
        return cost;
    }

    public List<String> getPath() {
        return path;
    }

    public String terminal() {
        return path.get(path.size() - 1);
    }

    public boolean contains(String key) {
        return path.contains(key);
    }

    @Override
    public String toString() {
        return String.join(" - ", path);
    }
}
