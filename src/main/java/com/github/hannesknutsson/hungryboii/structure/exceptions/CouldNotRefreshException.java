package com.github.hannesknutsson.hungryboii.structure.exceptions;

import java.io.IOException;

public class CouldNotRefreshException extends IOException {
    public CouldNotRefreshException(String msg, Exception e) {
        super(msg, e);
    }
}
