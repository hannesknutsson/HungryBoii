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

        Message message = Message.home(
                markdownSection("*Todays lunch* :fork_and_knife:"),
                divider(),
                markdownSection("*<https://www.kok11.se/dagens-lunch/%7CKök 11>*\nOpen 11:30-13:30  |  Price 115:-  |  Take-away/kuponger 105:-\n\n\t* Havets Wallenbergare med hummersås, juliennegrönsaker och potatisbakelse (LF)\n\t* Pulled pork med ugnsrostad klyftpotatis, rödkålsslaw och naanbröd (LF)\n\t* Vegetarisk kebab med pitabröd, vitlöksdressing, syrad lök och krispig sallad"),
                markdownSection("*<https://www.vaxjolakers.se/mat-dryck/lunchmeny%7CVida Arena>*\nOpen 11:30 - 14:00  |  Price 115:-  |  Arenakort 105:-\n\n\t* Helstekt karré med pepparsky och potatisgratäng (G)\n\t* Lasagne med lax och bladspenat\n\t* Fried rice med bbq-marinerad tofu (VGL)"),
                markdownSection("*<https://ostergatansrestaurang.se/lunch/%7CÖstergatan>*\nOpen 11:00 - 13:30  |  Price 99:-  |  Östergatankort 94:-\n\n\t* Fläskschnitzel med stekt potatis, gröna ärter & dragonsås\n\t* Janssonsfrestelse med stekt prinskorv & senap\n\t* Grönsaksfylld paprika med Quornfilé, bulgur & örtdressing "),
                markdownSection("*<https://www.restaurangfuturum.se/dagens-lunch%7CFuturum>*\nOpen 11:00 - 13:30  |  Price 98:-  |  Take-away 98:-\n\n\t* Wallenbergare med potatismos, lingon & rödvinssås\n\t* Rotfruktsgratäng med stekt quornfilé & örtcreme"),
                Blocks.actions(buttonElement())
        );

        return new Gson().toJson(message);

        //return format("<pre>%s</pre>", menu.getMenus());
    }

    @PostMapping("/")
    public String postMenus() {
        ListMenu menu = new ListMenu();
        return format("%s", menu.getMenus());
    }
}
