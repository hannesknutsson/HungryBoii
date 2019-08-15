package com.github.hannesknutsson.hungryboii.utilities.managers;

import com.github.hannesknutsson.hungryboii.structure.templates.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private static Logger LOG = LoggerFactory.getLogger(CommandManager.class);

    private static Map<String, Command> commandMap;

    public static boolean registerCommand(Command toRegister) {
        boolean success = false;
        if (!ApplicationManager.isRunning()) {
            Command potentiallyConflictingCommand = getCommandBySyntax(toRegister.getCommandSyntax());
            if (potentiallyConflictingCommand == null) {
                getCommandMap().put(toRegister.getCommandSyntax(), toRegister);
                LOG.info("Command registered: {}", toRegister.getCommandSyntax());
                success = true;
            } else {
                LOG.error("Aborting registration of command, command with the same syntax already registered: {}", potentiallyConflictingCommand.getCommandSyntax());
            }
        } else {
            LOG.error("Can not register command after application start: {}", toRegister.getCommandSyntax());
        }
        return success;
    }

    public static boolean unregisterCommand(String syntax) {
        boolean success = false;
        if (!ApplicationManager.isRunning()) {
            Command potentiallyRemovedCommand = getCommandMap().remove(syntax);
            if (potentiallyRemovedCommand != null) {
                LOG.info("Command unregistered: {}", potentiallyRemovedCommand.getCommandSyntax());
                success = true;
            } else {
                LOG.error("Failed to unregister command: {}", syntax);
            }
        } else {
            LOG.error("Can not unregister command after application start: {}", syntax);
        }
        return success;
    }

    public static Collection<Command> getAvailableCommands() {
        LOG.debug("Returned available commands");
        return getCommandMap().values();
    }

    public static Command getCommandBySyntax(String syntax) {
        LOG.debug("Command requested by syntax: {}", syntax);
        return getCommandMap().get(syntax);
    }

    private static Map<String, Command> getCommandMap() {
        if (commandMap == null) {
            commandMap = new HashMap<>();
        }
        return commandMap;
    }
}
