package com.github.hannesknutsson.hungryboii.structure.discord.events;

import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildMessageReceived extends ListenerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(GuildMessageReceived.class);

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            Command appropriateHandler = CommandManager.getInstance()
                    .getCommandBySyntax(event.getMessage().getContentRaw());
            if (appropriateHandler != null) {
                LOG.info("\"{}\" from the server \"{}\" requested \"{}\" in the text channel \"{}\"", event.getAuthor().getAsTag(), event.getMessage().getGuild().getName(), event.getMessage().getContentRaw(), event.getMessage().getChannel().getName());
                appropriateHandler.executeCommand(event); //Used to return a response that we could take care of
                //sendResponse(event, response);
            }
        }
    }

    //Saving these for later. I know this is not so very nice, please leave me alone.

    //private void sendResponse(GuildMessageReceivedEvent event, MessageAction response) {
    //   response.queue(ourResponse -> {
    //       CommandToReplyMapper.addCommandReplyPair(ourResponse, event.getMessage());
    //       addReactionOptions(ourResponse);
    //   });
    //}

    //private void addReactionOptions(Message message) {
    //    List<String> availableReactionOptions = ReactionActionManager.getInstance().getAllAvailableReactionActions().stream().map(ReactionAction::getActivator).collect(Collectors.toList());
    //    availableReactionOptions.stream().forEach(actionTrigger -> message.addReaction(actionTrigger).queue());
    //}
}
