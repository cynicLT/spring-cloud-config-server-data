package org.springframework.cloud.config.server.data.environment;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.data.repository.DataRepository;
import org.springframework.cloud.config.server.data.structure.DataPropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DataEnvironment implements EnvironmentRepository, InitializingBean {
    private static final String DEFAULT_PROFILE_NAME = "default";

    private final InternalYamlProcessor internalYamlProcessor;
    private final DataRepository dataRepository;

    public DataEnvironment(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.internalYamlProcessor = new InternalYamlProcessor();

        afterPropertiesSet();
    }

    @Override
    public Environment findOne(String application, String profile, String label) {
        String[] profilesArray = StringUtils.commaDelimitedListToStringArray(profile);
        List<String> profilesList = createProfiles(profilesArray);
        List<String> labelsList = createLabels(label);

        try {
            List<DataPropertySource> dataPropertySourceList = sortJdbcPropertySourceBy(
                    sortJdbcPropertySourceBy(
                            dataRepository.loadPropertySources(application, profilesList, labelsList),
                            Comparator.comparingInt(source -> labelsList.indexOf(source.getLabel()))
                    ),
                    Comparator.comparingInt(source -> profilesList.indexOf(source.getProfile()))
            );

            Environment environment = new Environment(application, profilesArray, label, null, null);

            environment.addAll(dataPropertySourceList.stream().
                    map(jdbcPropertySource -> new PropertySource(
                                    createSourceName(application, jdbcPropertySource),
                                    internalYamlProcessor.flattenMap(jdbcPropertySource.getSource())
                            )
                    ).
                    collect(Collectors.toList())
            );

            return environment;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load environment configuration", e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (dataRepository == null) {
            throw new IllegalArgumentException("Property 'dataRepository' is required");
        }
    }

    private String createSourceName(String application, DataPropertySource propertySource) {
        String profile = StringUtils.isEmpty(propertySource.getProfile()) ? DEFAULT_PROFILE_NAME : propertySource.getProfile();
        String label = propertySource.getLabel();

        return StringUtils.isEmpty(label) ?
                String.format("%s-%s", application, profile) :
                String.format("%s-%s-%s", application, profile, label);
    }

    private List<String> createLabels(String label) {
        return new ArrayList<>(new LinkedHashSet<>(Arrays.asList(label, null)));
    }

    private List<String> createProfiles(String[] profilesArray) {
        List<String> profiles = new ArrayList<>(Arrays.asList(profilesArray.clone())).
                stream().
                map(profileItem -> DEFAULT_PROFILE_NAME.equals(profileItem) ? null : profileItem).
                collect(Collectors.toList());
        profiles.add(DEFAULT_PROFILE_NAME);

        return new ArrayList<>(new LinkedHashSet<>(profiles));
    }

    private List<DataPropertySource> sortJdbcPropertySourceBy(List<DataPropertySource> jdbcPropertySources, Comparator<DataPropertySource> comparator) {
        List<DataPropertySource> result = new ArrayList<>(jdbcPropertySources);

        result.sort(comparator);

        return result;
    }

    private class InternalYamlProcessor extends YamlProcessor {
        Map<String, Object> flattenMap(Map<String, Object> source) {
            return getFlattenedMap(source);
        }
    }
}
