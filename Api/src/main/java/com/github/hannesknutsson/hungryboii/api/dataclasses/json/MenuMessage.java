package com.github.hannesknutsson.hungryboii.api.dataclasses.json;

import java.util.List;

public record MenuMessage(List<Block> blocks, String response_type, boolean delete_original) {
    public static MenuMessage show(List<Block> blocks) {
        return new MenuMessage(blocks, "ephemeral", false);
    }

    public static MenuMessage share(List<Block> blocks) {
        return new MenuMessage(blocks, "in_channel", true);
    }
}
