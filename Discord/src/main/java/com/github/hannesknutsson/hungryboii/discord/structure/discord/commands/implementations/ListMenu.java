package com.github.hannesknutsson.hungryboii.discord.structure.discord.commands.implementations;

import com.github.hannesknutsson.hungryboii.discord.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.discord.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.EmbedHelper;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.ListMenuHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import static com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.TimeHelper.isWeekend;

public class ListMenu implements Command {

    @Override
    public String getCommandSyntax() {
        return "!hungry";
    }

    @Override
    public String getCommandDescription() {
        return "Lists daily menus at local lunch places in Växjö";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event) {

        EmbedBuilder embedObject = EmbedHelper.getCommandReplyEmbed(event);
        boolean weekend;

        MessageAction response;

        try {
            weekend = isWeekend();
            if (weekend) { //TODO change back to !weekend when done debugging
                response = sendMenuReply(embedObject, event);
            } else {
                response = sendWeekendReply(embedObject, event);
            }
        } catch (TotallyBrokenDudeException e) {
            response = sendApologyReply(embedObject, event);
        }

        response.queue();
    }

    private MessageAction sendMenuReply(EmbedBuilder embedObject, GuildMessageReceivedEvent event) {
        ListMenuHelper.getMenus(embedObject);
        embedObject.setTitle("Todays lunch alternatives");
        embedObject.setDescription("Before using the services of this bot, please consider using a låda in the future.\nThese are the lunch alternatives the restaurants are offering today:");

        return event.getChannel().sendMessage(embedObject.build());
    }

    private MessageAction sendWeekendReply(EmbedBuilder embedObject, GuildMessageReceivedEvent event) {
        embedObject.setTitle("Weekend");
        embedObject.setDescription("I'm not able to retrieve lunch alternatives for the restaurants on weekends.");

        return event.getChannel().sendMessage(embedObject.build());
    }

    private MessageAction sendApologyReply(EmbedBuilder embedObject, GuildMessageReceivedEvent event) {
        embedObject.setTitle("Ooopsie");
        embedObject.setDescription("I just received a TotallyBrokenDudeException. Something must have broken spectacularly!");

        return event.getChannel().sendMessage(embedObject.build());
    }
}
