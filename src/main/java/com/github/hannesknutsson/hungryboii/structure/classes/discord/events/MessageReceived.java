package com.github.hannesknutsson.hungryboii.structure.classes.discord.events;

import com.github.hannesknutsson.hungryboii.structure.templates.Command;
import com.github.hannesknutsson.hungryboii.utilities.managers.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReceived extends ListenerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(MessageReceived.class);

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            Command appropriateHandler = CommandManager.getCommandBySyntax(event.getMessage().getContentRaw());
            if (appropriateHandler != null) {
                appropriateHandler.executeCommand(event);
                LOG.info("\"{}\" from the server \"{}\" requested \"{}\" in the text channel \"{}\"", event.getAuthor().getAsTag(), event.getMessage().getGuild().getName(), event.getMessage().getContentRaw(), event.getMessage().getChannel().getName());
            }
        }
    }
}
