package com.github.hannesknutsson.hungryboii.structure.classes.discord.commands;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.templates.Command;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.managers.RestaurantManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ListMenu extends Command {

    public ListMenu() {
        super("!hungry");
    }

    @Override
    public String getCommandDescription() {
        return "Lists daily menus at local lunch places in Växjö";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event) {
        List<Restaurant> restaurants = RestaurantManager.getRegisteredRestaurants();

        EmbedBuilder embedBuilder = EmbedHelper.getCommandReplyEmbed(event);

        embedBuilder.setTitle("Todays lunch alternatives");
        embedBuilder.setDescription("These are the lunch alternatives the restaurants are offering today");

        for (Restaurant restaurant : restaurants) {
            MessageEmbed.Field addToEmbed;
            if (restaurant.getTodaysDishes().size() <= 0) {
                MessageEmbed.Field errorField = new MessageEmbed.Field(restaurant.getName(), "Having technical difficulties...", true);
                addToEmbed = errorField;
            } else {
                StringBuilder descriptionMaker = new StringBuilder();
                for (Dish dish : restaurant.getTodaysDishes()) {
                    descriptionMaker.append("    * ").append(dish.name).append("\n");
                }
                MessageEmbed.Field dishField = new MessageEmbed.Field(restaurant.getName(), descriptionMaker.toString(), true);
                addToEmbed = dishField;
            }
            embedBuilder.addField(addToEmbed);
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
