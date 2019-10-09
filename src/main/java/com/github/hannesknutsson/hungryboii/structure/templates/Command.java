package com.github.hannesknutsson.hungryboii.structure.templates;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

    String getCommandSyntax();

    String getCommandDescription();

    void executeCommand(GuildMessageReceivedEvent event);
}
