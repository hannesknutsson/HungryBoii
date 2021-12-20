package com.github.hannesknutsson.hungryboii.discord.structure.discord.events;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class InvitedToNewGuild extends ListenerAdapter {

    private Logger LOG = LoggerFactory.getLogger(InvitedToNewGuild.class);

    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        LOG.info("Yay! We got invited to a new guild!: \"{}\"", event.getGuild().getName());
    }
}
