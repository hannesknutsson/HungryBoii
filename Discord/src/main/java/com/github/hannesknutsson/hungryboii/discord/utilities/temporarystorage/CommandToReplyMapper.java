package com.github.hannesknutsson.hungryboii.discord.utilities.temporarystorage;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandToReplyMapper {

    private static Map<String, Message> commandToReplyMap;

    public static void addCommandReplyPair(Message reply, Message command) {
        getMap().put(reply.getId(), command);
    }

    public static Message getCommand(GuildMessageReceivedEvent reply) {
        return getMap().get(reply.getMessage().getId());
    }

    public static Message getCommand(String replyId) {
        return getMap().get(replyId);
    }

    private static Map<String, Message> getMap() {
        if (commandToReplyMap == null) {
            commandToReplyMap = new HashMap<>();
        }
        return commandToReplyMap;
    }
}
