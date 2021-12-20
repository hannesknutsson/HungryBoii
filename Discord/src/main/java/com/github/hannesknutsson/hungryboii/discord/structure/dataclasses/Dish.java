package com.github.hannesknutsson.hungryboii.discord.structure.dataclasses;

public class Dish {

    public final String name;

    public Dish(String name) {
        this.name = name.replace("\n", "");
    }

    public String toString() {
        return name;
    }
}
