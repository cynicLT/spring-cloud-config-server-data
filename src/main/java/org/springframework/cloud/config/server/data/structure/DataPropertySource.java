package org.springframework.cloud.config.server.data.structure;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataPropertySource {
    private String profile;
    private String label;
    private Map<String, Object> source = new LinkedHashMap<>();

    public DataPropertySource(String profile, String label, Map<String, Object> source) {
        this.profile = profile;
        this.label = label;
        this.source.putAll(source);
    }

    public String getProfile() {
        return profile;
    }


    public String getLabel() {
        return label;
    }

    public Map<String, Object> getSource() {
        return source;
    }
}
