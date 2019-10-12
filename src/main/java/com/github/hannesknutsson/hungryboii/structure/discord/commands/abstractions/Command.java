package com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public interface Command  {

    String getCommandSyntax();

    String getCommandDescription();

    MessageAction executeCommand(GuildMessageReceivedEvent event);
}
