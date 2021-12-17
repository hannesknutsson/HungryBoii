package com.github.hannesknutsson.hungryboii.api;


import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;

@RestController
public class HungryController {
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMenus() {
        ListMenu menu = new ListMenu();
        return format("%s", menu.getTextMenus());
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String postMenus() {
        ListMenu menu = new ListMenu();
        return menu.getSlackMenus();
    }

    @PostMapping(value = "/action")
    public void postToChannel(@RequestBody String payload) throws IOException {
        Gson gson = new Gson();

        Payload p = gson.fromJson(payload, Payload.class);

        URL url = p.response_url;
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");

        byte[] out = postMenus().getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }

    /**
     * POST http://localhost:8080/action
     *
     * {"type":"block_actions","team":{"id":"T9TK3CUKW","domain":"example"},"user":{"id":"UA8RXUSPL","username":"jtorrance","team_id":"T9TK3CUKW"},"api_app_id":"AABA1ABCD","token":"9s8d9as89d8as9d8as989","container":{"type":"message_attachment","message_ts":"1548261231.000200","attachment_id":1,"channel_id":"CBR2V3XEX","is_ephemeral":false,"is_app_unfurl":false},"trigger_id":"12321423423.333649436676.d8c1bb837935619ccad0f624c448ffb3","channel":{"id":"CBR2V3XEX","name":"review-updates"},"message":{"bot_id":"BAH5CA16Z","type":"message","text":"This content can't be displayed.","user":"UAJ2RU415","ts":"1548261231.000200"},"response_url":"https://hooks.slack.com/actions/AABA1ABCD/1232321423432/D09sSasdasdAS9091209","actions":[{"action_id":"WaXA","block_id":"=qXel","text":{"type":"plain_text","text":"View","emoji":true},"value":"click_me_123","type":"button","action_ts":"1548426417.840180"}]}
     */
    class Payload {
        Channel channel;
        URL response_url;
    }

    class Channel {
        String id;
        String name;
    }
}


/*

{
  "type": "block_actions",
  "team": {
    "id": "T9TK3CUKW",
    "domain": "example"
  },
  "user": {
    "id": "UA8RXUSPL",
    "username": "jtorrance",
    "team_id": "T9TK3CUKW"
  },
  "api_app_id": "AABA1ABCD",
  "token": "9s8d9as89d8as9d8as989",
  "container": {
    "type": "message_attachment",
    "message_ts": "1548261231.000200",
    "attachment_id": 1,
    "channel_id": "CBR2V3XEX",
    "is_ephemeral": false,
    "is_app_unfurl": false
  },
  "trigger_id": "12321423423.333649436676.d8c1bb837935619ccad0f624c448ffb3",
  "channel": {
    "id": "CBR2V3XEX",
    "name": "review-updates"
  },
  "message": {
    "bot_id": "BAH5CA16Z",
    "type": "message",
    "text": "This content can't be displayed.",
    "user": "UAJ2RU415",
    "ts": "1548261231.000200",
    ...
  },
  "response_url": "https://hooks.slack.com/actions/AABA1ABCD/1232321423432/D09sSasdasdAS9091209",
  "actions": [
    {
      "action_id": "WaXA",
      "block_id": "=qXel",
      "text": {
        "type": "plain_text",
        "text": "View",
        "emoji": true
      },
      "value": "click_me_123",
      "type": "button",
      "action_ts": "1548426417.840180"
    }
  ]
}
 */
