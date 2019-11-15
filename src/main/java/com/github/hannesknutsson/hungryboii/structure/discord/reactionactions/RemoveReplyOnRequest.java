package com.github.hannesknutsson.hungryboii.structure.discord.reactionactions;

import com.github.hannesknutsson.hungryboii.structure.discord.reactionactions.abstractions.SimpleReactionAction;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.DiscordHelper;
import com.github.hannesknutsson.hungryboii.utilities.temporarystorage.CommandToReplyMapper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class RemoveReplyOnRequest extends SimpleReactionAction {

    public RemoveReplyOnRequest() {
        super("✅");
    }

    @Override
    public String getShortActionDesctiption() {
        return "removal of reply";
    }

    @Override
    public void action(GuildMessageReactionAddEvent event) {
        /*
            TODO Maybe we should make sure that only the original user may remove the reply
                and also we should make sure we don't ty removing the messages from anyone else
        */
        if (event !=  null) {
            DiscordHelper.removeMessage(event.getMessageId(), event.getChannel());
            Message originalCommand = CommandToReplyMapper.getCommand(event.getMessageId());
            if (originalCommand != null) {
                originalCommand.addReaction("❌").queue();
            }
        }
    }
}
