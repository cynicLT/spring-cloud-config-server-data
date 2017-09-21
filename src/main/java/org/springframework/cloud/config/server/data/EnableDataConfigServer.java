package org.springframework.cloud.config.server.data;

import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.data.config.DataConfigServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Add this annotation to a {@code @Configuration} class to enable Spring Cloud Config
 * Server backed by JDBC compatible database.
 *
 * @author Kiril Nugmanov
 * @see EnableConfigServer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(DataConfigServerConfiguration.class)
@EnableConfigServer
public @interface EnableDataConfigServer {
}
