package com.github.hannesknutsson.hungryboii.structure.classes.discord.commands;

import com.github.hannesknutsson.hungryboii.structure.templates.Command;
import com.github.hannesknutsson.hungryboii.utilities.managers.CommandManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Help extends Command {

    Logger LOG = LoggerFactory.getLogger(Help.class);

    public Help() {
        super("!help");
    }

    private static MessageEmbed.Field getCommandAsField(Command toConvert) {
        return new MessageEmbed.Field(toConvert.getCommandSyntax(), toConvert.getCommandDescription(), false);
    }

    @Override
    public String getCommandDescription() {
        return "Displays this message";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = EmbedHelper.getCommandReplyEmbed(event);

        embedBuilder.setTitle("This is the amazing HungryBoii lunch menu bot");
        embedBuilder.setDescription("You have requested help, and help you shall receive!\nBelow are the commands available:");

        for (Command genericCommand : CommandManager.getAvailableCommands()) {
            embedBuilder.addField(getCommandAsField(genericCommand));
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();
        LOG.info("Responded to event with list of available commands and their descriptions!");
    }
}
