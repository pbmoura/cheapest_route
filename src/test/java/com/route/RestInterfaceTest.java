package com.route;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.route.core.Route;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.util.AbstractMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestInterfaceTest {

    public static final String ORIGIN = "GRU";
    public static final String DESTINATION = "SCL";
    public static final int COST = 10;

    public static class TestContollerTestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            App app = mock(App.class);
            Route route = new Route(ORIGIN);
            route.add(new AbstractMap.SimpleEntry<>(DESTINATION, COST));
            when(app.find(ORIGIN, DESTINATION)).thenReturn(route);
            new RestInterface(app).run();
        }
    }

    @ClassRule
    public static SparkServer<TestContollerTestSparkApplication> testServer = new SparkServer<>(RestInterfaceTest.TestContollerTestSparkApplication.class, 4567);

    @Test
    public void testGet() throws Exception {
        String path = String.format("/find/%s/%s", ORIGIN, DESTINATION);
        GetMethod get = testServer.get(path, false);
        HttpResponse httpResponse = testServer.execute(get);
        Route route = new ObjectMapper().readValue(new String(httpResponse.body()), Route.class);
        assertEquals(200, httpResponse.code());
        assertEquals(route.getCost(), COST);
        assertThat(route.getPath()).contains(ORIGIN, DESTINATION);
    }

    @Test
    public void testPost() throws Exception {
        String body = "{\n" +
                "  \"origin\": \"ORI\",\n" +
                "  \"destination\": \"DES\",\n" +
                "  \"cost\": \"1\"\n" +
                "}";
        PostMethod post = testServer.post("/add", body, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(201, httpResponse.code());
    }


}