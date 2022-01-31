package com.github.hannesknutsson.hungryboii.discord.configuration.subconfigs.discord;

import com.github.hannesknutsson.hungryboii.discord.configuration.ArgumentParser;
import com.github.hannesknutsson.hungryboii.discord.configuration.MasterConfiguration;
import com.typesafe.config.Config;

public class DiscordSettings {

    private static DiscordSettings instance;

    private Config discordConfig;

    private DiscordSettings() {
        discordConfig = MasterConfiguration.getInstance().getSubConfiguration("discord");
    }

    public static DiscordSettings getInstance() {
        if (instance == null) {
            instance = new DiscordSettings();
        }
        return instance;
    }

    public String getDiscordApiToken() {
        return ArgumentParser.getDiscordApiToken() == null ?
                discordConfig.getString("discordApiToken") : ArgumentParser.getDiscordApiToken();
    }
}
