package org.springframework.cloud.config.server.data;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.server.data.repository.DataRepository;
import org.springframework.cloud.config.server.data.structure.DataPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = InMemoryDataConfigServerTest.DataConfigServerApplication.class,
        webEnvironment = RANDOM_PORT
)
public class InMemoryDataConfigServerTest extends AbstractDataConfigServerTest {
    @SpringBootApplication
    @EnableDataConfigServer
    public static class DataConfigServerApplication {

        public static void main(String args[]) {
            SpringApplication.run(DataConfigServerApplication.class, args);
        }

        @Bean
        DataRepository dataRepository() {

            return (String application, List<String> profiles, List<String> labels) -> new ArrayList<DataPropertySource>() {{
                add(new DataPropertySource("default", "", new HashMap<String, Object>() {{
                    put("key", "value");
                }}));
            }};
        }
    }
}
