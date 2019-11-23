package com.github.hannesknutsson.hungryboii.structure.discord.events;

import com.github.hannesknutsson.hungryboii.structure.discord.reactionactions.abstractions.ReactionAction;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.ReactionActionManager;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class ReactionReceived extends ListenerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(GuildMessageReceived.class);

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if (!event.getUser().isBot()) {
            String reactionEmoji = event.getReactionEmote().getName();
            ReactionAction handler = ReactionActionManager.getInstance().getReactionActionByActivator(reactionEmoji);
            if (handler != null) {
                handler.action(event);
                LOG.info("\"{}\" from the server \"{}\" requested \"{}\" in the text channel \"{}\"", event.getUser().getAsTag(), event.getGuild().getName(), handler.getShortActionDesctiption(), event.getChannel().getName());
            }
        }
    }
}
