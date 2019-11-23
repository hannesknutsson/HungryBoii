package com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions;

import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class StaticReplyCommand implements Command {

    protected abstract EmbedBuilder staticReply();

    private EmbedBuilder replyTemplate;

    @Override
    public final void executeCommand(GuildMessageReceivedEvent event) {
        if (replyTemplate == null) {
            replyTemplate = staticReply();
        }

        event.getChannel().sendMessage(EmbedHelper.setEmbedFields(replyTemplate, event).build()).queue();
    }
}
