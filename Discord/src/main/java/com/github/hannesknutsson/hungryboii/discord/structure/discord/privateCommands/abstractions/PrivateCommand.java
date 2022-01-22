package com.github.hannesknutsson.hungryboii.discord.structure.discord.privateCommands.abstractions;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public interface PrivateCommand {

    String getCommandSyntax();

    void executeCommand(PrivateMessageReceivedEvent event);

    boolean adminOnly();

}
