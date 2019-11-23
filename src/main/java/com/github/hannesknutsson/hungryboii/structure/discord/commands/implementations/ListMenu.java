package com.github.hannesknutsson.hungryboii.structure.discord.commands.implementations;

import com.github.hannesknutsson.hungryboii.structure.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.structure.discord.commands.abstractions.Command;
import com.github.hannesknutsson.hungryboii.exceptions.TotallyBrokenDudeException;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.utilities.managers.implementations.RestaurantManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.discord.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.List;

import static com.github.hannesknutsson.hungryboii.utilities.statichelpers.TimeHelper.isWeekend;

public class ListMenu implements Command {

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

        EmbedBuilder embedObject = EmbedHelper.getCommandReplyEmbed(event);
        boolean weekend;

        MessageAction response;

        try {
            weekend = isWeekend();
            if (!weekend) {
                response = sendMenuReply(embedObject, event);
            } else {
                response = sendWeekendReply(embedObject, event);
            }
        } catch (TotallyBrokenDudeException e) {
            response = sendApologyReply(embedObject, event);
        }

        response.queue();
    }

    private MessageAction sendMenuReply(EmbedBuilder embedObject, GuildMessageReceivedEvent event) {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        for (Restaurant restaurant : restaurants) {
            MessageEmbed.Field addToEmbed;
            switch (restaurant.getStatus()) {
                case OK:
                    addToEmbed = compileMenu(restaurant);
                    break;
                case UNINITIALIZED:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "Has not yet been fetched from their website for the first time yet... You're one quick little bugger :)", false);
                    break;
                case WEBSITE_BROKEN:
                    addToEmbed = new MessageEmbed.Field(restaurant.getName(), "Seems to be having technical difficulties... (Probably my fault haha whatever)", false);
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
        embedObject.setDescription("Before using the services of this bot, please consider using a låda in the future.\nThese are the lunch alternatives the restaurants are offering today:");

        return event.getChannel().sendMessage(embedObject.build());
    }

    private MessageAction sendWeekendReply(EmbedBuilder embedObject, GuildMessageReceivedEvent event) {
        embedObject.setTitle("Weekend");
        embedObject.setDescription("I'm not able to retrieve lunch alternatives for the restaurants on weekends.");

        return event.getChannel().sendMessage(embedObject.build());
    }

    private MessageAction sendApologyReply(EmbedBuilder embedObject, GuildMessageReceivedEvent event) {
        embedObject.setTitle("Ooopsie");
        embedObject.setDescription("I just received a TotallyBrokenDudeException. Something must have broken spectacularly!");

        return event.getChannel().sendMessage(embedObject.build());
    }

    public MessageEmbed.Field compileMenu(Restaurant menuSource) {

        StringBuilder alternativeDescriptionBuilder = new StringBuilder();
        String restaurantInfo = "Open: " + menuSource.getOpenHours() + " | Price: " + menuSource.getPrice() + ":-\n";
        alternativeDescriptionBuilder.append(restaurantInfo);

        for (Dish dish : menuSource.getTodaysDishes()) {
            alternativeDescriptionBuilder.append("    * ").append(dish.name).append("\n");
        }
        return new MessageEmbed.Field(menuSource.getName(), alternativeDescriptionBuilder.toString(), false);
    }
}
