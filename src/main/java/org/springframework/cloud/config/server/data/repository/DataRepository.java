package org.springframework.cloud.config.server.data.repository;

import org.springframework.cloud.config.server.data.structure.DataPropertySource;

import java.util.List;

/**
 * Interface to load data from database
 */
public interface DataRepository {

    /**
     * Load Property sources from database;
     *
     * @param application application name
     * @param profiles    list of profiles
     * @param labels      list of labels
     * @return loaded property sources
     */
    List<DataPropertySource> loadPropertySources(String application, List<String> profiles, List<String> labels);
}
