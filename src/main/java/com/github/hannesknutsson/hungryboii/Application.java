package com.github.hannesknutsson.hungryboii;

import com.github.hannesknutsson.hungryboii.configuration.ArgumentParser;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.implementations.Futurum;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.implementations.Kok11;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.implementations.Ostergatan;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.implementations.VidaArena;
import com.github.hannesknutsson.hungryboii.structure.discord.commands.implementations.*;
import com.github.hannesknutsson.hungryboii.structure.discord.events.GuildMessageReceived;
import com.github.hannesknutsson.hungryboii.structure.discord.events.InvitedToNewGuild;
import com.github.hannesknutsson.hungryboii.structure.discord.events.PrivateMessageReceived;
import com.github.hannesknutsson.hungryboii.structure.discord.events.ReactionReceived;
import com.github.hannesknutsson.hungryboii.structure.discord.privateCommands.implementations.Announce;
import com.github.hannesknutsson.hungryboii.structure.discord.privateCommands.implementations.SetPresence;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.CommandManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.PrivateCommandManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.RestaurantManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.SubscriptionManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.hibernate.EntityCoupler;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.liquibase.DbUpdater;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.DiscordHelper;
import com.github.hannesknutsson.hungryboii.utilities.workers.MenuGatherer;
import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    private static boolean botHasStarted = false;

    public static void main(String[] args) {

        LOG.info("Initializing HungryBoii...");

        //Register any new commands here (derivatives of interface class "Command")
        CommandManager.getInstance().register(new ListMenu());
        CommandManager.getInstance().register(new Subscribe());
        CommandManager.getInstance().register(new Unsubscribe());
        CommandManager.getInstance().register(new Info());
        CommandManager.getInstance().register(new Help());

        //Register any new private commands here (derivatives of interface class "PrivateCommand")
        PrivateCommandManager.getInstance().register(new Announce());
        PrivateCommandManager.getInstance().register(new SetPresence());

        //Register any new reaction actions here (derivatives of interface class "ReactionAction")
        //Temporarily disabling this one. I sure look forward to using OSGI for this type of thing..
        //ReactionActionManager.getInstance().register(new RemoveReplyOnRequest());

        //Register any new restaurants here (implementations of interface "Restaurant")
        RestaurantManager.getInstance().register(new Futurum());
        RestaurantManager.getInstance().register(new Ostergatan());
        RestaurantManager.getInstance().register(new Kok11());
        RestaurantManager.getInstance().register(new VidaArena());

        start(args);
    }

    private static Boolean start(String[] args) {
        if (!botHasStarted && ArgumentParser.parseArguments(args)) {
            try {
                DbUpdater.verifyAndUpdateDatabase();
                EntityCoupler.initialize();
                DiscordHelper.initialize();
                SubscriptionManager.initialize();
                DiscordHelper.addEventlistener(new GuildMessageReceived());
                DiscordHelper.addEventlistener(new InvitedToNewGuild());
                DiscordHelper.addEventlistener(new PrivateMessageReceived());
                DiscordHelper.addEventlistener(new ReactionReceived());
                MenuGatherer.startGathering();
                LOG.debug("Application started successfully!");
                botHasStarted = true;
            } catch (LoginException | IllegalArgumentException | LiquibaseException e) {
                LOG.error("Application failed to start, {} received", e.toString());
                botHasStarted = false;
            }

            return botHasStarted;
        } else {
            LOG.error("Application failed to start, already running");
            return false;
        }
    }
}
