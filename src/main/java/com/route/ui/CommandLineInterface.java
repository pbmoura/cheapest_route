package com.route.ui;

import com.route.App;
import com.route.core.Route;

import java.io.Console;

public class CommandLineInterface {
    private final App app;

    public CommandLineInterface(App app) {
        this.app = app;
    }

    public void run() {
        Console console = System.console();
        String routeInput;
        do {
            routeInput = console.readLine("please enter the route: ");
            try {
                if (!routeInput.isEmpty()) {
                    Route route = find(routeInput);
                    System.out.println(String.format("best route: %s > %d", route.toString(), route.getCost()));
                }
            } catch (Exception e) {
                System.out.println("Error searching for route " + routeInput);
            }
        } while (true);
    }

    public Route find(String route) {
        String[] parts = route.split("-");
        return app.find(parts[0], parts[1]);
    }
}
