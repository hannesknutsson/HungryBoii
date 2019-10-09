package com.github.hannesknutsson.hungryboii.utilities.managers;

import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordTaskManager {

    private static Logger LOG = LoggerFactory.getLogger(DiscordTaskManager.class);

    private DiscordTaskManager() {};

    public static User getUserById(final long userId) {
        User requestedUser = ApplicationManager.getMainJDAObject().getUserById(userId);
        LOG.debug("The user ID \"{}\" belongs to \"{}\"", userId, requestedUser.getAsTag());
        return requestedUser;
    }
}
