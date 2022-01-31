package com.github.hannesknutsson.hungryboii.discord.utilities.managers.implementations;

import com.github.hannesknutsson.hungryboii.discord.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.discord.utilities.managers.abstractions.MappingManager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends MappingManager<String, Command> {

    private static CommandManager commandManager;

    public List<Command> getAvailableCommands() {
        return new ArrayList<>(getRegisteredObjects().values());
    }

    public void register(Command toRegister) {
        register(getFirstWord(toRegister.getCommandSyntax()), toRegister);
    }

    public Command getCommandBySyntax(String syntax) {
        return getRegisteredObjects().get(getFirstWord(syntax));
    }

    private String getFirstWord(String sentence) {
        return sentence.split(" ", 2)[0];
    }

    @Override
    public String getManagerType() {
        return "Command";
    }

    public static CommandManager getInstance() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }
        return commandManager;
    }
}
