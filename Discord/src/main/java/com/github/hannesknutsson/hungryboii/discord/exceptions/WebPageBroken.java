package com.github.hannesknutsson.hungryboii.discord.exceptions;

import java.io.IOException;

public class WebPageBroken extends IOException {

    public WebPageBroken(Exception e) {
        super(e);
    }
}
