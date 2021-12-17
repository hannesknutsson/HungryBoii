package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Message;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.*;
import static java.lang.String.format;

@RestController
public class HungryController {
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMenus() {
        ListMenu menu = new ListMenu();
        return format("%s", menu.getTextMenus());
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String postMenus() {
        ListMenu menu = new ListMenu();
        return menu.getSlackMenus();
    }
}
