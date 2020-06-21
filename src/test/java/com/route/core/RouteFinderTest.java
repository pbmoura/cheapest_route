package com.route.core;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.Assert.assertNull;

public class RouteFinderTest {

    private static RouteFinder finder;

    @BeforeClass
    public static void setup() {
        Map<String, Map<String, Integer>> costs = new HashMap<>();
        Map<String, Integer> costA = new HashMap<>();
        costA.put("B", 3);
        costA.put("D", 8);
        costs.put("A", costA);
        Map<String, Integer> costB = new HashMap<>();
        costB.put("C", 1);
        costB.put("D", 3);
        costB.put("A", 2);
        costs.put("B", costB);
        Map<String, Integer> costC = new HashMap<>();
        costC.put("E", 2);
        costs.put("C", costC);
        finder = new RouteFinder(costs);
    }

    @Test
    public void directPath() {
        Route route = finder.find("A", "B");
        assertNotNull(route);
        assertEquals(3, route.getCost());
        assertThat(route.getPath())
                .hasSize(2)
                .contains("A", "B");
    }

    @Test
    public void noRoute() {
        Route route = finder.find("A", "Z");
        assertNull(route);
    }

    @Test
    public void twoHops() {
        Route route = finder.find("A", "C");
        assertNotNull(route);
        assertEquals(4, route.getCost());
        assertThat(route.getPath())
                .hasSize(3)
                .contains("A", "B", "C");
    }

    @Test
    public void twoHopsExistingShorrterButCostier() {
        Route route = finder.find("A", "D");
        assertNotNull(route);
        assertEquals(6, route.getCost());
        assertThat(route.getPath())
                .hasSize(3)
                .contains("A", "B", "D");
    }

    @Test
    public void threeHops() {
        Route route = finder.find("A", "E");
        assertNotNull(route);
        assertEquals(6, route.getCost());
        assertThat(route.getPath())
                .hasSize(4)
                .contains("A", "B", "C", "E");
    }

    @Test
    public void addRoutes() {
        finder.handleEntry("x", "y", 1);
        finder.handleEntry("x", "w", 2);
        finder.handleEntry("y", "w", 3);

        assertThat(finder.costs).containsKeys("x", "y");
        assertThat(finder.costs.get("x")).contains(entry("y", 1), entry("w", 2));
        assertThat(finder.costs.get("y")).contains(entry("w", 3));
    }

}