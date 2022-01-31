package com.github.hannesknutsson.hungryboii.discord.utilities.managers.implementations;

import com.github.hannesknutsson.hungryboii.discord.structure.discord.privateCommands.abstractions.PrivateCommand;
import com.github.hannesknutsson.hungryboii.discord.utilities.managers.abstractions.MappingManager;

import java.util.ArrayList;
import java.util.List;

public class PrivateCommandManager extends MappingManager<String, PrivateCommand> {

    private static PrivateCommandManager privateCommandManager;

    public List<PrivateCommand> getAvailablePrivateCommands() {
        return new ArrayList<>(getRegisteredObjects().values());
    }

    public void register(PrivateCommand toRegister) {
        register(getFirstWord(toRegister.getCommandSyntax()), toRegister);
    }

    public PrivateCommand getCommandBySyntax(String syntax) {
        return getRegisteredObjects().get(getFirstWord(syntax));
    }

    private String getFirstWord(String sentence) {
        return sentence.split(" ", 2)[0];
    }

    @Override
    public String getManagerType() {
        return "PrivateCommand";
    }

    public static PrivateCommandManager getInstance() {
        if (privateCommandManager == null) {
            privateCommandManager = new PrivateCommandManager();
        }
        return privateCommandManager;
    }
}
