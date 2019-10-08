package com.github.hannesknutsson.hungryboii.structure.classes.discord.commands;

import com.github.hannesknutsson.hungryboii.structure.classes.discord.events.MessageReceived;
import com.github.hannesknutsson.hungryboii.structure.templates.StaticReplyCommand;
import com.github.hannesknutsson.hungryboii.utilities.managers.DiscordTaskManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Info extends StaticReplyCommand {

    Logger LOG = LoggerFactory.getLogger(Info.class);

    @Override
    public String getCommandSyntax() {
        return "!info";
    }

    @Override
    public String getCommandDescription() {
        return "Displays some information regarding this bot";
    }

    @Override
    protected EmbedBuilder staticReply() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("HungryBoii INFO");
        embedBuilder.setDescription("Here is some information regarding this very bot");

        MessageEmbed.Field genericInfo = new MessageEmbed.Field("About", "This is a simple Discord bot for retrieving lunch alternatives at local restaurants to simplify making the choice of where to go.", false);
        MessageEmbed.Field gitHubField = new MessageEmbed.Field("GitHub repository", "https://github.com/hannesknutsson/HungryBoii", false);
        MessageEmbed.Field dockerHubField = new MessageEmbed.Field("DockerHub image", "https://cloud.docker.com/u/hannesknutsson/repository/docker/hannesknutsson/hungryboii", false);
        MessageEmbed.Field inviteField = new MessageEmbed.Field("Invite this bot to your server", "https://discordapp.com/oauth2/authorize?client_id=610906357339652259&permissions=67584&scope=bot", false);

        String hankeMention = DiscordTaskManager.getUserById(234711759309504513L);
        MessageEmbed.Field contributors = new MessageEmbed.Field("Contributors", "So far it is only " + hankeMention + " that has put his heart into this beautiful creation :cry:", false);

        embedBuilder.addField(genericInfo);
        embedBuilder.addField(gitHubField);
        embedBuilder.addField(dockerHubField);
        embedBuilder.addField(inviteField);
        embedBuilder.addField(contributors);

        return embedBuilder;
    }
}