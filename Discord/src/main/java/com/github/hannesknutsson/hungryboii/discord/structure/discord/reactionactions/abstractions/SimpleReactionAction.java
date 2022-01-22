package com.github.hannesknutsson.hungryboii.discord.structure.discord.reactionactions.abstractions;

public abstract class SimpleReactionAction implements ReactionAction {

    private String activator;

    protected SimpleReactionAction(String activator) {
        this.activator = activator;
    }

    @Override
    public String getActivator() {
        return activator;
    }
}
