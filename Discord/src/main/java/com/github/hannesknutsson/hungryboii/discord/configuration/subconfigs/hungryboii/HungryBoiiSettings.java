package com.github.hannesknutsson.hungryboii.discord.configuration.subconfigs.hungryboii;

import com.github.hannesknutsson.hungryboii.discord.configuration.MasterConfiguration;
import com.typesafe.config.Config;

public class HungryBoiiSettings {

    private static HungryBoiiSettings instance;

    private Config hungryBoiiConfig;

    private HungryBoiiSettings() {
        hungryBoiiConfig = MasterConfiguration.getInstance().getSubConfiguration("hungryBoii");
    }

    public static HungryBoiiSettings getInstance() {
        if (instance == null) {
            instance = new HungryBoiiSettings();
        }
        return instance;
    }

    public long getAdminId() {
        return hungryBoiiConfig.getLong("adminId");
    }
}
