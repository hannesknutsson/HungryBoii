package com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class EmbedHelper {

    private static Color embedColor = Color.green;

    public static EmbedBuilder getCommandReplyEmbed(GuildMessageReceivedEvent event) {
        EmbedBuilder toReturn = new EmbedBuilder();
        setEmbedFields(toReturn, event);
        return toReturn;
    }

    public static EmbedBuilder setEmbedFields(EmbedBuilder toGenerify, GuildMessageReceivedEvent event) {
        return setEmbedFields(toGenerify, event.getAuthor());
    }

    public static EmbedBuilder setEmbedFields(EmbedBuilder embedBuilder, User user) {
        embedBuilder.setColor(embedColor);
        embedBuilder.setFooter("Request made by " + user.getAsTag(), user.getAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        return embedBuilder;
    }
}
