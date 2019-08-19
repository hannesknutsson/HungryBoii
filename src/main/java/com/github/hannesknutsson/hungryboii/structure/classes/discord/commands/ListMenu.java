package com.github.hannesknutsson.hungryboii.structure.classes.discord.commands;

import com.github.hannesknutsson.hungryboii.structure.classes.Dish;
import com.github.hannesknutsson.hungryboii.structure.templates.Command;
import com.github.hannesknutsson.hungryboii.structure.templates.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.managers.RestaurantManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListMenu implements Command {

    Logger LOG = LoggerFactory.getLogger(ListMenu.class);

    @Override
    public String getCommandSyntax() {
        return "!hungry";
    }

    @Override
    public String getCommandDescription() {
        return "Lists daily menus at local lunch places in Växjö";
    }

    @Override
    public void executeCommand(GuildMessageReceivedEvent event) {

        List<Restaurant> restaurants = RestaurantManager.getRegisteredRestaurants();
        EmbedBuilder embedObject = EmbedHelper.getCommandReplyEmbed(event);

        for (Restaurant restaurant : restaurants) {
            MessageEmbed.Field addToEmbed = null;
            switch (restaurant.getStatus()) {
                case OK:
                    addToEmbed = compileMenu(restaurant);
                    break;
                case UNINITIALIZED:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "Has not yet been fetched from their website for the first time yet... You're one quick little bugger :)", false);
                    break;
                case WEBSITE_BROKEN:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "Seems to be having technical difficulties... (their fault, not mine)", false);
                    break;
                case PARSING_BROKEN:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "The parsing for this restaurants website has broken. Why do they update that sort of stuff anyway? (my fault, not theirs)", false);
                    break;
                case WEEKEND:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "I do not provide lunch alternatives on weekdays. You should never see this message in the wild...", false);
                    break;
                default:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "If you see this, something has gone terribly wrong...", false);
                    break;
            }
            embedObject.addField(addToEmbed);
        }

        embedObject.setTitle("Todays lunch alternatives");
        embedObject.setDescription("These are the lunch alternatives the restaurants are offering today");

        event.getChannel().sendMessage(embedObject.build()).queue();
        LOG.info("{} requested the menus", event.getAuthor().getAsTag());
    }

    private MessageEmbed.Field compileMenu(Restaurant menuSource) {
        StringBuilder alternativeDescriptionBuilder = new StringBuilder();
        for (Dish dish : menuSource.getTodaysDishes()) {
            alternativeDescriptionBuilder.append("    * ").append(dish.name).append("\n");
        }
        return new MessageEmbed.Field(menuSource.getName(), alternativeDescriptionBuilder.toString(), false);
    }

}
