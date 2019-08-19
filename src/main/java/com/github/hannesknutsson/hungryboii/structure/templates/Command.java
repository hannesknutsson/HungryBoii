package com.github.hannesknutsson.hungryboii.structure.templates;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract interface Command {

    public String getCommandSyntax();

    public abstract String getCommandDescription();

    public abstract void executeCommand(GuildMessageReceivedEvent event);
}
