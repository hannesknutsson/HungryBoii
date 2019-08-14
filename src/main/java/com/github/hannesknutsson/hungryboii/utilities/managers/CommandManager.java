package com.github.hannesknutsson.hungryboii.utilities.managers;

import com.github.hannesknutsson.hungryboii.structure.templates.Command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private static Map<String, Command> commandMap;

    public static void registerCommand(Command toRegister) {
        getCommandMap().put(toRegister.getCommandSyntax(), toRegister);
    }

    public static boolean unregisterCommand(String syntax) {
        return getCommandMap().remove(syntax) != null;
    }

    public static Collection<Command> getAvailableCommands() {
        return getCommandMap().values();
    }

    public static Command getCommandBySyntax(String syntax) {
        return getCommandMap().get(syntax);
    }

    private static Map<String, Command> getCommandMap() {
        if (commandMap == null) {
            commandMap = new HashMap<>();
        }
        return commandMap;
    }
}
