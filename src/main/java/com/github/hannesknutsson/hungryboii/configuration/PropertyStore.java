package com.github.hannesknutsson.hungryboii.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class PropertyStore {

    private static PropertyStore instance;

    private final Config configuration;

    private PropertyStore() {
        configuration = ConfigFactory.parseFile(new File(ArgumentParser.getConfigurationPath()));
    }

    public static PropertyStore getInstance() {
        if (instance == null) {
            instance = new PropertyStore();
        }
        return instance;
    }

    public String getDiscordApiToken() {
        return ArgumentParser.getDiscordApiToken() == null ?
                configuration.getString("discordApiToken") : ArgumentParser.getDiscordApiToken();
    }
}
