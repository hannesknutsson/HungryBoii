package com.github.hannesknutsson.hungryboii.discord.structure.discord.privateCommands.implementations;

import com.github.hannesknutsson.hungryboii.discord.structure.discord.privateCommands.abstractions.PrivateCommand;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.DiscordHelper;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class Announce implements PrivateCommand {


    @Override
    public String getCommandSyntax() {
        return "!announce";
    }

    @Override
    public void executeCommand(PrivateMessageReceivedEvent event) {
        String completeMessage = event.getMessage().getContentRaw();
        String[] splitArr = completeMessage.split(" ", 2);

        if (splitArr.length == 2) {
            EmbedBuilder embed = EmbedHelper.getCommandReplyEmbedNoUser();
            String announcement = splitArr[1].trim();

            embed.setTitle("Announcement");
            embed.setDescription(announcement);

            List<TextChannel> announcementReceivers = getAnnouncementChannels();

            announcementReceivers.forEach(textChannel -> {
                textChannel.sendMessage(embed.build()).queue();
            });
        }
    }

    @Override
    public boolean adminOnly() {
        return true;
    }

    private List<TextChannel> getAnnouncementChannels() {
        List<Guild> allGuilds = DiscordHelper.getAllConnectedGuilds();
        List<TextChannel> announcementReceivers = new ArrayList<>();

        for (Guild guild : allGuilds) {
            TextChannel defaultChannel = guild.getDefaultChannel();
            if (defaultChannel.canTalk()) {
                announcementReceivers.add(defaultChannel);
            } else {
                for (TextChannel textChannel : guild.getTextChannels()) {
                    if (textChannel.canTalk()) {
                        announcementReceivers.add(textChannel);
                    }
                }
            }
        }

        return announcementReceivers;
    }
}
