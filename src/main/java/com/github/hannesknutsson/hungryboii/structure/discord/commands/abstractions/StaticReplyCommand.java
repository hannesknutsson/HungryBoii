package com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions;

import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public abstract class StaticReplyCommand implements Command {

    protected abstract EmbedBuilder staticReply();

    private EmbedBuilder replyTemplate;

    @Override
    public final MessageAction executeCommand(GuildMessageReceivedEvent event) {
        if (replyTemplate == null) {
            replyTemplate = staticReply();
        }

        return event.getChannel().sendMessage(EmbedHelper.setEmbedFields(replyTemplate, event).build());
    }
}
