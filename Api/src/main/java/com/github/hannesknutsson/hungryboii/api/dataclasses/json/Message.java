package com.github.hannesknutsson.hungryboii.api.dataclasses.json;

import java.util.List;

public class Message {
    private final String type;
    private final List<Block> blocks;

    public Message(String type, List<Block> blocks) {
        this.type = type;
        this.blocks = blocks;
    }

    public static Message home(List<Block> blocks) {
        return new Message("home", blocks);
    }

    public String getType() {
        return type;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
