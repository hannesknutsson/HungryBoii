package com.github.hannesknutsson.hungryboii.utilities.managers;

public class DiscordTaskManager {

    private DiscordTaskManager() {};

    public static String getUserById(final long userId) {
        return ApplicationManager.getMainJDAObject().getUserById(userId).getAsMention();
    }
}
