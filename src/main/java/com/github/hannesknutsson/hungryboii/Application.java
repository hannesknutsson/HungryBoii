package com.github.hannesknutsson.hungryboii;

import com.github.hannesknutsson.hungryboii.structure.classes.discord.commands.Help;
import com.github.hannesknutsson.hungryboii.structure.classes.discord.commands.Info;
import com.github.hannesknutsson.hungryboii.structure.classes.discord.commands.ListMenu;
import com.github.hannesknutsson.hungryboii.structure.classes.restaurants.Futurum;
import com.github.hannesknutsson.hungryboii.structure.classes.restaurants.Kok11;
import com.github.hannesknutsson.hungryboii.structure.classes.restaurants.Ostergatan;
import com.github.hannesknutsson.hungryboii.utilities.managers.ApplicationManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.CommandManager;
import com.github.hannesknutsson.hungryboii.utilities.managers.RestaurantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        LOG.info("Initializing HungryBoii...");

        //Register any new commands here (derivatives of abstract class "Command")
        CommandManager.registerCommand(new Help());
        CommandManager.registerCommand(new ListMenu());
        CommandManager.registerCommand(new Info());

        //Register any new restaurants here (implementations of interface "Restaurant")
        RestaurantManager.registerRestaurant(new Futurum());
        RestaurantManager.registerRestaurant(new Ostergatan());
        RestaurantManager.registerRestaurant(new Kok11());

        ApplicationManager.start(args);
    }
}
