package com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord;

import com.github.hannesknutsson.hungryboii.configuration.subconfigs.discord.DiscordSettings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DiscordHelper {

    private static Logger LOG = LoggerFactory.getLogger(DiscordHelper.class);

    private static JDA discordBot;

    private static Map<Long, User> frequentUserMap;

    private DiscordHelper() {};

    public static void removeMessage(String messageId, TextChannel channel) {
        channel.deleteMessageById(messageId).queue();
    }

    public static User getUserById(final long userId) {
        User requestedUser = getFrequentUser(userId) == null ? getFrequentUser(userId) : rawGetUserById(userId);
        LOG.debug("The user ID \"{}\" belongs to \"{}\"", userId, requestedUser.getAsTag());
        return requestedUser;
    }

    public static void addEventlistener(EventListener toAdd) {
        discordBot.addEventListener(toAdd);
    }

    public static void initialize() throws LoginException {
        if (discordBot == null) {
            discordBot = new JDABuilder(DiscordSettings.getInstance().getDiscordApiToken()).build();
        }
    }

    private static User getFrequentUser(Long userId) {
        return getFrequentUserMap().get(userId);
    }

    private static Map<Long, User> getFrequentUserMap() {
        if (frequentUserMap == null) {
            frequentUserMap = new ConcurrentHashMap<>();
            frequentUserMap.put(234711759309504513L, rawGetUserById(234711759309504513L));
        }
        return frequentUserMap;
    }

    private static User rawGetUserById(long userId) {
        return discordBot.getUserById(userId);
    }

    void asda() {
        User asda;
    }
}
