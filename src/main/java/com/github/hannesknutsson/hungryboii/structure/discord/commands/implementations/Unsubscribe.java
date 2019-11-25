package com.github.hannesknutsson.hungryboii.structure.discord.commands.implementations;

import com.github.hannesknutsson.hungryboii.structure.dataclasses.DiscordUser;
import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.SubscriptionManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.hibernate.EntityCoupler;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.hibernate.Session;

public class Unsubscribe implements Command {

    @Override
    public String getCommandSyntax() {
        return "!unsubscribe";
    }

    @Override
    public String getCommandDescription() {
        return "Removes your current subscription if you no longer want to receive daily updates on the lunch alternatives.";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event) {
        EmbedBuilder toReturn = EmbedHelper.getCommandReplyEmbed(event);
        try (Session temporarySession = EntityCoupler.getInstance().getSession()) {
            temporarySession.beginTransaction();

            DiscordUser user = temporarySession.find(DiscordUser.class, event.getAuthor().getIdLong());
            toReturn.setTitle("Cancellation of daily subscription");

            if (user != null && user.getLunchSubscription() != null) {
                user.setLunchSubscription(null);
                temporarySession.saveOrUpdate(user);
                temporarySession.flush();
                SubscriptionManager.getInstance().unRegister(user.getId());
                toReturn.setDescription("Successfully removed your subscription! :point_right::sunglasses::point_right:\n\nI am truly happy to see you taking the a step towards\nliving la vida låda! :heart_eyes:");
            } else {
                toReturn.setDescription("Could not find any subscription in your name :sob:");
            }
        }
        event.getChannel().sendMessage(toReturn.build()).queue();
    }
}
