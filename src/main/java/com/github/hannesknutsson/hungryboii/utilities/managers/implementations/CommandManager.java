package com.github.hannesknutsson.hungryboii.utilities.managers.implementations;

import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.utilities.managers.abstractions.MappingManager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends MappingManager<String, Command> {

    private static CommandManager commandManager;

    public List<Command> getAvailableCommands() {
        return new ArrayList<>(getRegisteredObjects().values());
    }

    public boolean register(Command toRegister) {
        return register(toRegister.getCommandSyntax(), toRegister);
    }

    public Command getCommandBySyntax(String syntax) {
        return getRegisteredObjects().get(syntax);
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
