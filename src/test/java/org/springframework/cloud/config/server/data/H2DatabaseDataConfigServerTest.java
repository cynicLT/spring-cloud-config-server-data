package org.springframework.cloud.config.server.data;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.server.data.repository.DataRepository;
import org.springframework.cloud.config.server.data.structure.DataPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = H2DatabaseDataConfigServerTest.DataConfigServerApplication.class,
        webEnvironment = RANDOM_PORT
)
@ActiveProfiles("h2")
public class H2DatabaseDataConfigServerTest extends AbstractDataConfigServerTest {
    @SpringBootApplication
    @EnableDataConfigServer
    public static class DataConfigServerApplication {

        public static void main(String args[]) {
            SpringApplication.run(H2DatabaseDataConfigServerTest.DataConfigServerApplication.class, args);
        }

        @Bean
        DataRepository dataRepository(JdbcTemplate jdbcTemplate) {
            return (String application, List<String> profiles, List<String> labels) ->
                    jdbcTemplate.query("select * from settings",
                            (rs, rowNum) -> new DataPropertySource(
                                    rs.getString("profile"),
                                    rs.getString("label"),
                                    new HashMap<String, Object>() {{
                                        put(rs.getString("source"), rs.getString("value"));
                                    }}
                            )
                    );
        }
    }
}
