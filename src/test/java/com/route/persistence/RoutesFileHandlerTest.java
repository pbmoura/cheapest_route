package com.route.persistence;

import com.route.core.RouteFileListener;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class RoutesFileHandlerTest {

    @Test
    public void shouldReadFile() {
        String fileName = "input-file.txt";
        ClassLoader classLoader = RoutesFileHandler.class.getClassLoader();
        String filePath = classLoader.getResource(fileName).getFile();
        List<String> lines = new ArrayList<>();
        RoutesFileHandler fileHandler = new RoutesFileHandler(filePath, (origin, destination, cost) -> lines.add(String.format("%s,%s,%d", origin, destination, cost)));
        fileHandler.read();
        assertThat(lines).contains("GRU,BRC,10", "BRC,SCL,5", "GRU,CDG,75", "GRU,SCL,20", "GRU,ORL,56", "ORL,CDG,5", "SCL,ORL,20", "GRU,NAT,10", "NAT,REC,1");
    }

    @Test
    public void shouldAddLine() throws Exception {
        String filePath = "file";
        StringBuilder sb = new StringBuilder();
        File file = new File(filePath);
        try {
            RoutesFileHandler fileHandler = new RoutesFileHandler(filePath, (origin, destination, cost) -> sb.append(String.format("%s-%s-%d", origin, destination, cost)));
            fileHandler.add("origin", "destination", 10);
            assertEquals("origin-destination-10", sb.toString());
            List<String> lines = Files.readAllLines(file.toPath());
            assertEquals(1, lines.size());
            assertEquals("origin,destination,10", lines.get(0));
        } finally {
            file.delete();
        }
    }

}