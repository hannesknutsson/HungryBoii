package com.github.hannesknutsson.hungryboii.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class MasterConfiguration {

    private static MasterConfiguration instance;
    private final Config configuration;

    private MasterConfiguration() {
        configuration = ConfigFactory.parseFile(new File(ArgumentParser.getConfigurationPath()));
    }

    public static MasterConfiguration getInstance() {
        if (instance == null) {
            instance = new MasterConfiguration();
        }
        return instance;
    }

    public Config getSubConfiguration(String key) {
        return configuration.getConfig(key);
    }
}
