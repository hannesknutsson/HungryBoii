package com.github.hannesknutsson.hungryboii.discord.structure.discord.reactionactions.abstractions;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public interface ReactionAction {

    String getShortActionDesctiption();

    String getActivator();

    void action(GuildMessageReactionAddEvent event);

}
