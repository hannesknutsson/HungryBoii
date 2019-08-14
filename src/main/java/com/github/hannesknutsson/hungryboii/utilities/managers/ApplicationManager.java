package com.github.hannesknutsson.hungryboii.utilities.managers;

import com.github.hannesknutsson.hungryboii.configuration.ArgumentParser;
import com.github.hannesknutsson.hungryboii.structure.classes.discord.events.MessageReceived;
import com.github.hannesknutsson.hungryboii.utilities.workers.MenuGatherer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class ApplicationManager {

    private static boolean botHasStarted = false;

    public static Boolean start(String [] args) {
        if(!botHasStarted && ArgumentParser.parseArguments(args)) {
            try {
                JDA discordBot = new JDABuilder(ArgumentParser.getDiscordApiToken()).build();
                discordBot.addEventListener(new MessageReceived());
                MenuGatherer.startGathering();
                botHasStarted = true;
            } catch (LoginException | IllegalArgumentException e) {
                botHasStarted = false;
            }

            return botHasStarted;
        } else {
            return false;
        }
    }

    public static boolean isRunning() {
        return botHasStarted;
    }




}
