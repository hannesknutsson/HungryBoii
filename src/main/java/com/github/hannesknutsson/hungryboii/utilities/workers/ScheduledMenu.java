package com.github.hannesknutsson.hungryboii.utilities.workers;

import com.github.hannesknutsson.hungryboii.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.structure.discord.commands.implementations.ListMenu;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.CommandManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.RestaurantManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.DiscordHelper;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMenu implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(ScheduledMenu.class);

    private Long userId;

    public ScheduledMenu(Long userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        LOG.info("Daily subscription for {} activated", userId);
        User user = DiscordHelper.getUserById(userId);
        LOG.info("User object retrieved from discord API for {}", user.getAsTag());
        user.openPrivateChannel().queue(privateChannel -> {
            LOG.info("Private channel object retrieved from discord API for {}", user.getAsTag());
            try {
                if (!TimeHelper.isWeekend()) {
                    LOG.info("It's not a weekend {}", user.getAsTag());

                    EmbedBuilder toSend = EmbedHelper.setEmbedFields(new EmbedBuilder(), user);

                    //I know this doesn't look good, but this is to prepare for the future OSGI implementation transformation
                    ListMenu lister = (ListMenu) CommandManager.getInstance().getAvailableCommands().stream().filter(command -> command instanceof ListMenu).findAny().get();

                    //This does not look so very good either, but don't judge me. It will all be very glorious soon
                    for (Restaurant restaurant : RestaurantManager.getInstance().getRegisteredRestaurants()) {
                        MessageEmbed.Field menuField = lister.compileMenu(restaurant);
                        toSend.addField(menuField);
                    }

                    toSend.setTitle("Your daily subscription");
                    toSend.setDescription("This is your daily subscription. You can cancel this at any time by typing \"!unsubscribe\" in the server chat (we don't support private chat commands yet..).");

                    LOG.info("Sending daily subscription to {}", user.getAsTag());

                    privateChannel.sendMessage(toSend.build()).queue();
                }
            } catch (TotallyBrokenDudeException e) {
                LOG.error("Received a TotallyBrokenDudeException when trying to send {} their daily subscription", user.getAsTag());
            }
            LOG.info("Done with the subscription for {} for today", user.getAsTag());
        });
    }
}
