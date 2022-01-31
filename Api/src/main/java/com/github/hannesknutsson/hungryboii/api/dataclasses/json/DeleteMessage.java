package com.github.hannesknutsson.hungryboii.api.dataclasses.json;

public record DeleteMessage(String response_type, boolean replace_original, boolean delete_original, String text) {
    public static DeleteMessage deleteEphemeral() {
        return new DeleteMessage("ephemeral", true, true, "");
    }
}