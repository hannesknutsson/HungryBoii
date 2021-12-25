package com.github.hannesknutsson.hungryboii.discord.utilities.workers;

import com.github.hannesknutsson.hungryboii.discord.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.TimeHelper;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.DiscordHelper;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.EmbedHelper;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.ListMenuHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledMenu implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledMenu.class);

    private final Long userId;

    public ScheduledMenu(Long userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        User discordUser = DiscordHelper.getUserById(userId);
        LOG.info("Daily subscription for \"{}\" activated", discordUser.getAsTag());
        discordUser.openPrivateChannel().queue(privateChannel -> {
            try {
                if (!TimeHelper.isWeekend()) {

                    EmbedBuilder toSend = EmbedHelper.setEmbedFields(new EmbedBuilder(), discordUser);

                    ListMenuHelper.getMenus(toSend);

                    toSend.setTitle("Your daily subscription");
                    toSend.setDescription("This is your daily subscription. You can cancel this at any time by typing \"!unsubscribe\" in the server chat (we don't support private chat commands yet..).");

                    LOG.info("Sending daily subscription to \"{}\"", discordUser.getAsTag());

                    privateChannel.sendMessage(toSend.build()).queue();
                }
            } catch (TotallyBrokenDudeException e) {
                LOG.error("Received a TotallyBrokenDudeException when trying to send {} their daily subscription", discordUser.getAsTag());
            }
        });
    }
}
