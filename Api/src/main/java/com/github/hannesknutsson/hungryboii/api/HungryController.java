package com.github.hannesknutsson.hungryboii.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HungryController {
    @GetMapping("/")
    public String index() {
        return """
                Foo
                Bar
                Baz
                """;
    }
}
