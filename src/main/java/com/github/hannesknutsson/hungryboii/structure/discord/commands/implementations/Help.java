package com.github.hannesknutsson.hungryboii.structure.discord.commands.implementations;

import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.StaticReplyCommand;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.CommandManager;
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

        for (Command genericCommand : CommandManager.getInstance().getAvailableCommands()) {
            embedBuilder.addField(getCommandAsField(genericCommand));
        }

        embedBuilder.setTitle("HungryBoii Commands");
        embedBuilder.setDescription("Following are the commands available for use with this exquisite bot");

        return embedBuilder;
    }

    private static MessageEmbed.Field getCommandAsField(Command toConvert) {
        return new MessageEmbed.Field(toConvert.getCommandSyntax(), toConvert.getCommandDescription(), false);
    }
}
