package com.github.hannesknutsson.hungryboii.api.dataclasses.json;

import java.util.List;

public class Blocks {
    private static final Divider DIVIDER = new Divider();

    private Blocks() {}

    public static Divider divider() {
        return DIVIDER;
    }

    public static MarkdownSection markdownSection(String text) {
        return new MarkdownSection(new MarkdownText(text));
    }

    public static Header header(String text) {
        return new Header(text);
    }

    public static class Divider implements Block {
        private String type = "divider";
    }

    public static Actions actions(Element... elements) {
        return new Actions(List.of(elements));
    }

    public static ShareButton shareButton() {
        return new ShareButton();
    }

    public static CloseButton closeButton() {
        return new CloseButton();
    }

    public static class Header implements Block {
        private String type = "header";
        private final PlainText text;

        public Header(String text) {
            this.text = new PlainText(text);
        }
    }

    public static class MarkdownSection implements Block {
        private String type = "section";
        private final MarkdownText text;

        public MarkdownSection(MarkdownText text) {
            this.text = text;
        }
    }

    public static class MarkdownText implements Block {
        private String type = "mrkdwn";
        private final String text;

        public MarkdownText(String text) {
            this.text = text;
        }
    }

    public static class PlainText implements Block {
        private String type = "plain_text";
        private boolean emoji = true;
        private final String text;

        public PlainText(String text) {
            this.text = text;
        }
    }

    public static class Actions implements Block {
        private String type = "actions";
        private List<Element> elements;

        public Actions(List<Element> elements) {
            this.elements = elements;
        }
    }

    public interface Element {} // marker

    public static class ShareButton implements Element {
        private String type = "button";
        private ButtonText text = new ButtonText("Send");
        private String value = "share_menu";
        private String action_id = "actionId-0";
        private String style = "primary";
    }

    public static class CloseButton implements Element {
        private String type = "button";
        private ButtonText text = new ButtonText("Close");
        private String value = "close";
        private String action_id = "actionId-1";
    }

    public static class ButtonText {
        private String type = "plain_text";
        private String text;
        private boolean emoji = true;

        public ButtonText(String text) {
            this.text = text;
        }
    }
}
