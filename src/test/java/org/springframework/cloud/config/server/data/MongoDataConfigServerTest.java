package org.springframework.cloud.config.server.data;

import org.junit.runner.RunWith;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.server.data.repository.DataRepository;
import org.springframework.cloud.config.server.data.structure.DataPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = MongoDataConfigServerTest.DataConfigServerApplication.class,
        webEnvironment = RANDOM_PORT
)
public class MongoDataConfigServerTest extends AbstractDataConfigServerTest {

    @SpringBootApplication
    @EnableDataConfigServer
    public static class DataConfigServerApplication {

        public static void main(String args[]) {
            SpringApplication.run(H2DatabaseDataConfigServerTest.DataConfigServerApplication.class, args);
        }

        @Bean
        CommandLineRunner preLoadMongo(MongoTemplate mongoTemplate) throws Exception {
            return args -> {
                mongoTemplate.save(new DataPropertySource("", "", new HashMap<String, Object>() {{
                    put("key", "value");
                }}), "my-app");
            };
        }

        @Bean
        DataRepository dataRepository(MongoTemplate mongoTemplate) {
            return (String application, List<String> profiles, List<String> labels) ->
                    mongoTemplate.find(new Query(), DataPropertySource.class, application);
        }
    }
}
