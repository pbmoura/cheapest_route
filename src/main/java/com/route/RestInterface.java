package com.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.route.core.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestInterface {

    private final App app;

    public RestInterface(App app) {
        this.app = app;
    }

    private void add(RoutePayload payload) throws IOException {
        app.addRoute(payload.origin, payload.destination, payload.cost);
    }

    private static String asJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error converting to JSON");
        }
    }

    public void run() {
        get("/find/:origin/:destination", (request, response) -> {
            String origin = request.params("origin");
            String destination = request.params("destination");
            Route route = app.find(origin, destination);
            response.status(HttpURLConnection.HTTP_OK);
            response.type("application/json");
            return asJson(route);
        });
        post("/add", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                RoutePayload payload = mapper.readValue(request.body(), RoutePayload.class);
                this.add(payload);
                response.status(HttpURLConnection.HTTP_CREATED);

            } catch (Exception e) {
                response.status(HttpURLConnection.HTTP_BAD_REQUEST);
                e.printStackTrace();
            }
            return "";
        });
    }

    static class RoutePayload {
        String origin;
        String destination;
        int cost;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }
    }
}
