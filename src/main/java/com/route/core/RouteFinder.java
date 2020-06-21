package com.route.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RouteFinder implements RouteFileListener {
    final Map<String, Map<String, Integer>> costs;

    public RouteFinder(Map<String, Map<String, Integer>> costs) {
        this.costs = costs;
    }

    public RouteFinder() {
        costs = new HashMap<>();
    }

    public Route find(String origin, String destination) {
        Queue<Route> candidateRoutes = new LinkedList<>();
        Route bestRoute = null;
        expand(candidateRoutes, new Route(origin));
        while (!candidateRoutes.isEmpty()) {
            Route route = candidateRoutes.poll();
            if (destination.equals(route.terminal())) {
                if (bestRoute == null || route.getCost() < bestRoute.getCost())
                    bestRoute = route;
            } else {
                if (bestRoute == null || route.getCost() < bestRoute.getCost())
                    expand(candidateRoutes, route);
            }
        }

        return bestRoute;
    }

    private void expand(Queue<Route> candidateRoutes, Route route) {
        if (costs.containsKey(route.terminal())) {
            for (Map.Entry<String, Integer> entry : costs.get(route.terminal()).entrySet()) {
                if (!route.contains(entry.getKey())) {
                    Route newRoute = new Route(route);
                    newRoute.add(entry);
                    candidateRoutes.add(newRoute);
                }
            }
        }
    }

    @Override
    public void handleEntry(String origin, String destination, int cost) {
        Map<String, Integer> originMap = costs.computeIfAbsent(origin, k -> new HashMap<>());
        originMap.put(destination, cost);
    }
}
