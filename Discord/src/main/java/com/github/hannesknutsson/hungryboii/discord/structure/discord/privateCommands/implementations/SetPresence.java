package com.github.hannesknutsson.hungryboii.discord.structure.discord.privateCommands.implementations;

import com.github.hannesknutsson.hungryboii.discord.structure.discord.privateCommands.abstractions.PrivateCommand;
import com.github.hannesknutsson.hungryboii.discord.utilities.statichelpers.discord.DiscordHelper;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class SetPresence implements PrivateCommand {

    @Override
    public String getCommandSyntax() {
        return "!presence";
    }

    @Override
    public void executeCommand(PrivateMessageReceivedEvent event) {
        String completeMessage = event.getMessage().getContentRaw();

        String[] splitArr = completeMessage.split(" ", 3);

        if (splitArr.length == 3) {
            String type = splitArr[1];
            String description = splitArr[2];

            Activity activity = getActivity(type, description);
            if (activity != null) {
                DiscordHelper.setPresence(activity);
            }
        } else if (splitArr.length == 2 && splitArr[1].trim().toUpperCase().equals("RESET")) {
            DiscordHelper.setPresence(null);
        }
    }

    @Override
    public boolean adminOnly() {
        return true;
    }

    private Activity getActivity(String type, String description) {
        switch (type.trim().toUpperCase()) {
            case "WATCHING":
                return Activity.watching(description);
            case "LISTENING":
                return Activity.listening(description);
            case "PLAYING":
                return Activity.playing(description);
            default:
                return null;
        }
    }
}
