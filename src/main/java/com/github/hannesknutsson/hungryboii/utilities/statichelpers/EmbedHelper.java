package com.github.hannesknutsson.hungryboii.utilities.statichelpers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class EmbedHelper {

    public static EmbedBuilder getCommandReplyEmbed(GuildMessageReceivedEvent event) {
        EmbedBuilder toReturn = new EmbedBuilder();
        toReturn.setColor(Color.green);
        toReturn.setFooter("Request made by " + event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());
        toReturn.setTimestamp(Instant.now());
        return toReturn;
    }
}
