package com.github.hannesknutsson.hungryboii.structure.classes.discord.commands;

import com.github.hannesknutsson.hungryboii.structure.templates.Command;
import com.github.hannesknutsson.hungryboii.structure.templates.StaticReplyCommand;
import com.github.hannesknutsson.hungryboii.utilities.managers.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Help extends StaticReplyCommand {

    @Override
    public String getCommandSyntax() {
        return "!help";
    }

    @Override
    public String getCommandDescription() {
        return "Displays this message";
    }

    @Override
    protected EmbedBuilder staticReply() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        for (Command genericCommand : CommandManager.getAvailableCommands()) {
            embedBuilder.addField(getCommandAsField(genericCommand));
        }

        return embedBuilder;
    }

    private static MessageEmbed.Field getCommandAsField(Command toConvert) {
        return new MessageEmbed.Field(toConvert.getCommandSyntax(), toConvert.getCommandDescription(), false);
    }
}
