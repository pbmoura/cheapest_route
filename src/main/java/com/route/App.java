package com.route;

import com.route.core.Route;
import com.route.core.RouteFinder;
import com.route.persistence.RoutesFileHandler;
import com.route.ui.CommandLineInterface;
import com.route.ui.RestInterface;

import java.io.IOException;

public class App {

    private final RouteFinder routeFinder;
    private final RoutesFileHandler fileHandler;

    public App(String filePath) {
        routeFinder = new RouteFinder();
        fileHandler = new RoutesFileHandler(filePath, routeFinder);
    }

    public static void main(String... args) {
        validateArgs(args);
        App app = new App(args[0]);
        app.loadData();
        CommandLineInterface cli = new CommandLineInterface(app);
        RestInterface restInterface = new RestInterface(app);
        new Thread(restInterface::run).start();
        cli.run();
    }

    protected static void validateArgs(String[] args) {
        if (args.length == 0) {
            System.out.println("input file required");
            System.exit(1);
        }
    }

    protected void loadData() {
        if (!fileHandler.read()) {
            System.out.println("Could not read input file");
            System.exit(1);
        }
    }

    public Route find(String origin, String destination) {
        return routeFinder.find(origin, destination);
    }

    public void addRoute(String origin, String destination, int cost) throws IOException {
        fileHandler.add(origin, destination, cost);
    }
}
