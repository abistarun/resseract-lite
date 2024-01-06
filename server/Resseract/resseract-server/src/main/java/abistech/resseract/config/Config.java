package abistech.resseract.config;

import abistech.resseract.util.Constants;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ToString
public class Config {

    private static final Config EMPTY_CONFIG = new Config();
    private Map<String, Object> properties;

    public Config() {
        this.properties = new HashMap<>();
    }

    public Config(Config config) {
        this.properties = new HashMap<>(config.properties);
    }

    public Object get(ConfigKey key) {
        return this.properties.get(key.getKey());
    }

    public Object get(String key) {
        return this.properties.get(key);
    }

    public Object get(ConfigKey key, String identifier) {
        if (identifier == null)
            return get(key);
        return this.properties.get(key.getKey() + Constants.UNDERSCORE + identifier);
    }

    public void put(ConfigKey key, Object property) {
        this.properties.put(key.getKey(), property);
    }

    public void put(ConfigKey key, String identifier, Object property) {
        if (identifier == null)
            put(key, property);
        this.properties.put(key.getKey() + Constants.UNDERSCORE + identifier, property);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(properties, config.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    public static Config emptyConfig() {
        return EMPTY_CONFIG;
    }

    public void remove(ConfigKey key) {
        this.properties.remove(key.getKey());
    }

    public void append(Config configurations) {
        if (configurations == null || configurations.getProperties() == null)
            return;
        this.properties.putAll(configurations.getProperties());
    }
}
