package com.route.persistence;

import com.route.core.RouteFileListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class RoutesFileHandler {
    private final String filePath;
    private final RouteFileListener listener;

    public RoutesFileHandler(String filePath, RouteFileListener listener) {
        this.filePath = filePath;
        this.listener = listener;
    }

    public boolean read() {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> {
                String[] parts = line.split(",");
                listener.handleEntry(parts[0], parts[1], Integer.parseInt(parts[2]));
            });
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void add(String origin, String destination, int cost) throws IOException {
        String inputLine = String.format("%s,%s,%d%s", origin, destination, cost, System.lineSeparator());
        Files.write(Paths.get(filePath), inputLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        listener.handleEntry(origin, destination, cost);
    }
}
