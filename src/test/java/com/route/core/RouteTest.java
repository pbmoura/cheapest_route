package com.route.core;

import org.junit.Test;

import java.util.AbstractMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class RouteTest {

    @Test
    public void createEntry() {
        Route route = new Route("O");
        assertEquals(0, route.getCost());
        assertThat(route.getPath())
                .hasSize(1)
                .contains("O");
    }

    @Test
    public void addOneEntry() {
        Route route = new Route("O");
        route.add(new AbstractMap.SimpleEntry<>("A", 1));
        assertEquals(1, route.getCost());
        assertThat(route.getPath())
                .hasSize(2)
                .contains("O", "A");
    }

    @Test
    public void addTwoEntries() {
        Route route = new Route("O");
        route.add(new AbstractMap.SimpleEntry<>("B", 1));
        route.add(new AbstractMap.SimpleEntry<>("C", 3));
        assertEquals(4, route.getCost());
        assertThat(route.getPath())
                .hasSize(3)
                .contains("O", "B", "C");
    }

    @Test
    public void copyContructor() {
        Route route = new Route("A");
        Route copy = new Route(route);
        copy.add(new AbstractMap.SimpleEntry<>("B", 1));
        assertEquals(0, route.getCost());
        assertEquals(1, copy.getCost());
        assertThat(route.getPath()).hasSize(1).contains("A");
        assertThat(copy.getPath()).hasSize(2).contains("A", "B");

    }

}