package com.github.hannesknutsson.hungryboii.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
public class HungryController {
    @GetMapping("/")
    public String index() {
        ListMenu menu = new ListMenu();
        return format("<pre>%s</pre>", menu.getMenus());
    }
}
