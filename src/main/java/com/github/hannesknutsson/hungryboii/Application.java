package com.github.hannesknutsson.hungryboii;

import com.github.hannesknutsson.hungryboii.structure.classes.discord.commands.Help;
import com.github.hannesknutsson.hungryboii.structure.classes.discord.commands.ListMenu;
import com.github.hannesknutsson.hungryboii.structure.classes.restaurants.Futurum;
import com.github.hannesknutsson.hungryboii.structure.classes.restaurants.Ostergatan;
import com.github.hannesknutsson.hungryboii.utilities.managers.ApplicationManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.CommandManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.RestaurantManager;

public class Application {

    public static void main(String[] args) {

        //Register any new commands here (derivatives of abstract class "Command")
        CommandManager.registerCommand(new Help());
        CommandManager.registerCommand(new ListMenu());

        //Register any new restaurants here (implementations of interface "Restaurant")
        RestaurantManager.registerRestaurant(new Futurum());
        RestaurantManager.registerRestaurant(new Ostergatan());

        ApplicationManager.start(args);
    }
}
