package com.github.hannesknutsson.hungryboii.structure.classes.discord.events;

import com.github.hannesknutsson.hungryboii.utilities.managers.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceived extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            try {
                CommandManager.getCommandBySyntax(event.getMessage().getContentRaw()).executeCommand(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
