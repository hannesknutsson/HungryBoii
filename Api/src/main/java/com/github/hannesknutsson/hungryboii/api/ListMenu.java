package com.github.hannesknutsson.hungryboii.api;

import com.github.hannesknutsson.hungryboii.api.dataclasses.Dish;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks;
import com.github.hannesknutsson.hungryboii.api.dataclasses.json.Message;
import com.github.hannesknutsson.hungryboii.api.dataclasses.restaurants.abstractions.Restaurant;
import com.github.hannesknutsson.hungryboii.api.enumerations.RestaurantStatus;
import com.github.hannesknutsson.hungryboii.api.managers.implementations.RestaurantManager;
import com.google.gson.Gson;

import java.util.List;

import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.buttonElement;
import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.divider;
import static com.github.hannesknutsson.hungryboii.api.dataclasses.json.Blocks.markdownSection;
import static java.lang.String.format;

public class ListMenu {

    public String getTextMenus() {
        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();
        StringBuilder response = new StringBuilder();
        for (Restaurant restaurant : restaurants) {
            switch (restaurant.getStatus()) {
                case OK -> response.append(compileMenu(restaurant));
                case UNINITIALIZED -> response.append(format("%s: Has not yet been fetched from their website for the first time yet... You're one quick little bugger :)\n", restaurant.getName()));
                case WEBSITE_BROKEN -> response.append(format("%s: Seems to be having technical difficulties... (Probably my fault haha whatever)\n", restaurant.getName()));
                case PARSING_BROKEN -> response.append(format("%s: The parsing for this restaurants website has broken. Why do they update that sort of stuff anyway? (my fault, not theirs)\n", restaurant.getName()));
                case WEEKEND -> response.append(format("%s: I do not provide lunch alternatives on weekdays. You should never see this message in the wild...\n", restaurant.getName()));
                default -> response.append(format("%s: If you see this, something has gone terribly wrong...\n", restaurant.getName()));
            }
        }

        return response.toString();
    }

    public String getSlackMenus() {
/*        List<Restaurant> restaurants = RestaurantManager.getInstance().getRegisteredRestaurants();

        List<Blocks.MarkdownSection> sections;
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus().equals(RestaurantStatus.OK)) {
                String sectionText = format("*<%s>");
                sections.add(markdownSection(""));
            }
        }*/

        Message message = Message.home(
                markdownSection("*Todays lunch* :fork_and_knife:"),
                divider(),
                markdownSection(":one:  *<https://www.kok11.se/dagens-lunch/|Kök 11>*\nOpen 11:30-13:30  |  Price 115:-  |  Take-away/kuponger 105:-\n\n\t* Havets Wallenbergare med hummersås, juliennegrönsaker och potatisbakelse (LF)\n\t* Pulled pork med ugnsrostad klyftpotatis, rödkålsslaw och naanbröd (LF)\n\t* Vegetarisk kebab med pitabröd, vitlöksdressing, syrad lök och krispig sallad"),
                markdownSection(":two:  *<https://www.vaxjolakers.se/mat-dryck/lunchmeny|Vida Arena>*\nOpen 11:30 - 14:00  |  Price 115:-  |  Arenakort 105:-\n\n\t* Helstekt karré med pepparsky och potatisgratäng (G)\n\t* Lasagne med lax och bladspenat\n\t* Fried rice med bbq-marinerad tofu (VGL)"),
                markdownSection(":three:  *<https://ostergatansrestaurang.se/lunch/|Östergatan>*\nOpen 11:00 - 13:30  |  Price 99:-  |  Östergatankort 94:-\n\n\t* Fläskschnitzel med stekt potatis, gröna ärter & dragonsås\n\t* Janssonsfrestelse med stekt prinskorv & senap\n\t* Grönsaksfylld paprika med Quornfilé, bulgur & örtdressing "),
                markdownSection(":four:  *<https://www.restaurangfuturum.se/dagens-lunch|Futurum>*\nOpen 11:00 - 13:30  |  Price 98:-  |  Take-away 98:-\n\n\t* Wallenbergare med potatismos, lingon & rödvinssås\n\t* Rotfruktsgratäng med stekt quornfilé & örtcreme"),
                Blocks.actions(buttonElement())
        );
        return new Gson().toJson(message);
    }

    private String compileMenu(Restaurant menuSource) {

        StringBuilder alternativeDescriptionBuilder = new StringBuilder();
        String restaurantInfo = "Open: " + menuSource.getOpenHours() + " | Price: " + menuSource.getPrice() + ":-\n";
        alternativeDescriptionBuilder.append(restaurantInfo);

        for (Dish dish : menuSource.getTodaysDishes()) {
            alternativeDescriptionBuilder.append("    * ").append(dish.name).append("\n");
        }
        return format("%s: %s", menuSource.getName(), alternativeDescriptionBuilder.toString());
    }
}
