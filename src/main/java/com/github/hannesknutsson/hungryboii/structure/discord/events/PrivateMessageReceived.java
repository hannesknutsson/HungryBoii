package com.github.hannesknutsson.hungryboii.structure.discord.events;

import com.github.hannesknutsson.hungryboii.configuration.subconfigs.hungryboii.HungryBoiiSettings;
import com.github.hannesknutsson.hungryboii.structure.discord.privateCommands.abstractions.PrivateCommand;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.PrivateCommandManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateMessageReceived extends ListenerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(PrivateMessageReceived.class);

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            PrivateCommand appropriatePrivateCommand = PrivateCommandManager.getInstance()
                    .getCommandBySyntax(event.getMessage().getContentRaw());
            if (appropriatePrivateCommand != null) {
                if (shouldPrivateCommandBeExecuted(appropriatePrivateCommand, event.getAuthor())) {
                    LOG.info("\"{}\" requested \"{}\" in the private text channel", event.getAuthor().getAsTag(), event.getMessage().getContentRaw());
                    appropriatePrivateCommand.executeCommand(event);
                } else {
                    LOG.warn("\"{}\" requested \"{}\" in the private text channel without being admin!", event.getAuthor().getAsTag(), event.getMessage().getContentRaw());
                }
            }
        }
    }

    private boolean shouldPrivateCommandBeExecuted(PrivateCommand privateCommand, User user) {
        return !privateCommand.adminOnly() || user.getIdLong() == HungryBoiiSettings.getInstance().getAdminId();
    }
}
