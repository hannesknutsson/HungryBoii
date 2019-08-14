package com.github.hannesknutsson.hungryboii.structure.templates;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {

    private String syntax;

    protected Command(String syntax) {
        this.syntax = syntax;
    }

    public String getCommandSyntax() {
        return syntax;
    }

    public abstract String getCommandDescription();

    public abstract void executeCommand(GuildMessageReceivedEvent event);
}
