package com.github.hannesknutsson.hungryboii.utilities.managers;

import com.github.hannesknutsson.hungryboii.configuration.ArgumentParser;
import com.github.hannesknutsson.hungryboii.structure.classes.discord.events.InvitedToNewGuild;
import com.github.hannesknutsson.hungryboii.structure.classes.discord.events.MessageReceived;
import com.github.hannesknutsson.hungryboii.utilities.workers.MenuGatherer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class ApplicationManager {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private static boolean botHasStarted = false;

    private static JDA discordBot;

    public static Boolean start(String[] args) {
        if (!botHasStarted && ArgumentParser.parseArguments(args)) {
            try {
                discordBot = new JDABuilder(ArgumentParser.getDiscordApiToken()).build();
                discordBot.addEventListener(new MessageReceived());
                discordBot.addEventListener(new InvitedToNewGuild());
                MenuGatherer.startGathering();
                LOG.info("Application started successfully!");
                botHasStarted = true;
            } catch (LoginException | IllegalArgumentException e) {
                LOG.error("Application failed to start, {} received", e.toString());
                botHasStarted = false;
            }

            return botHasStarted;
        } else {
            LOG.error("Application failed to start, already running");
            return false;
        }
    }

    public static boolean isRunning() {
        return botHasStarted;
    }

    protected static JDA getMainJDAObject() {
        return discordBot;
    }
}
