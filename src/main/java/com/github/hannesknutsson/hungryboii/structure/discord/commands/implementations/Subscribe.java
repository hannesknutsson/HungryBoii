package com.github.hannesknutsson.hungryboii.structure.discord.commands.implementations;

import com.github.hannesknutsson.hungryboii.structure.dataclasses.DiscordUser;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.LunchSubscription;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.Time;
import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.SubscriptionManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.hibernate.EntityCoupler;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.hibernate.Session;

public class Subscribe implements Command {


    @Override
    public String getCommandSyntax() {
        return "!subscribe <hour>:<minute>";
    }

    @Override
    public String getCommandDescription() {
        return "Lets you subscribe to the menus of the day. This will be sent to you in private every day at a time you request.";
    }

    @Override
    public MessageAction executeCommand(GuildMessageReceivedEvent event) {
        EmbedBuilder toReturn;
        if (validateInput(event)) {
            //Do cool stuff
            try (Session temporarySession = EntityCoupler.getInstance().getSession()) {
                temporarySession.beginTransaction();
                DiscordUser user = temporarySession.find(DiscordUser.class, event.getAuthor().getIdLong());
                if (user == null) {
                    user = new DiscordUser(event.getAuthor().getIdLong());
                }

                boolean hadExistingSub = user.getLunchSubscription() != null;
                LunchSubscription newSub = getLunchSubscriptionFromGuildMessageReceivedEvent(event);
                user.setLunchSubscription(newSub);

                temporarySession.persist(newSub);
                temporarySession.saveOrUpdate(user);
                temporarySession.flush();

                SubscriptionManager.getInstance().registerIfRelevant(user);

                toReturn = EmbedHelper.getCommandReplyEmbed(event);
                toReturn.setTitle("Subscription successfully added!");
                toReturn.setDescription("You will hear from me each working day at " + user.getLunchSubscription() + "!");
            }
        } else {
            //User really seem to have done goofed hehe, let them know.
            toReturn = EmbedHelper.getCommandReplyEmbed(event);
            toReturn.setTitle("Something went wrong");
            toReturn.setDescription("That's all I'm going to tell you :sweat_smile:");
        }

        return event.getChannel().sendMessage(toReturn.build());
    }

    private boolean validateInput(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (!message.matches("!subscribe ([0-9]{1,2}:[0-9]{1,2})")) {
            return false;
        }

        String timeString = message.split(" ", 2)[1];
        String[] timeArr = timeString.split(":", 2);

        int hour = Integer.parseInt(timeArr[0]);
        int minute = Integer.parseInt(timeArr[1]);

        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            return false;
        }

        return true;
    }

    private LunchSubscription getLunchSubscriptionFromGuildMessageReceivedEvent(GuildMessageReceivedEvent event) {
        LunchSubscription lunchSubscription;
        String message = event.getMessage().getContentRaw();
        if (validateInput(event)) {
            String timeString = message.split(" ", 2)[1];
            String[] timeArr = timeString.split(":", 2);

            int hour = Integer.parseInt(timeArr[0]);
            int minute = Integer.parseInt(timeArr[1]);

            lunchSubscription = new LunchSubscription(hour, minute);
        } else {
            lunchSubscription = null;
        }
        return lunchSubscription;
    }
}
