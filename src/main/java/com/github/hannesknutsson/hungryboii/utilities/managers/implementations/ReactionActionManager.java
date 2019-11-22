package com.github.hannesknutsson.hungryboii.utilities.managers.implementations;

import com.github.hannesknutsson.hungryboii.structure.discord.reactionactions.abstractions.ReactionAction;
import com.github.hannesknutsson.hungryboii.utilities.managers.abstractions.MappingManager;

import java.util.ArrayList;
import java.util.List;

public class ReactionActionManager extends MappingManager<String, ReactionAction> {

    private static ReactionActionManager reactionActionManager;

    public void register(ReactionAction toRegister) {
        register(toRegister.getActivator(), toRegister);
    }

    public ReactionAction getReactionActionByActivator(String activator) {
        return getRegisteredObjects().get(activator);
    }

    public List<ReactionAction> getAllAvailableReactionActions() {
        return new ArrayList<>(getRegisteredObjects().values());
    }

    @Override
    public String getManagerType() {
        return "ReactionAction";
    }

    public static ReactionActionManager getInstance() {
        if (reactionActionManager == null) {
            reactionActionManager = new ReactionActionManager();
        }
        return reactionActionManager;
    }
}
