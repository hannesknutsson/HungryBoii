package com.github.hannesknutsson.hungryboii.api;


import com.github.hannesknutsson.hungryboii.api.dataclasses.json.DeleteMessage;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.hannesknutsson.hungryboii.api.ListMenu.*;

@RestController
@RequestMapping(path="api/hungryboii")
public class HungryController {
    @GetMapping()
    public String getTxtMenus() {
        return ListMenu.getTxtMenus();
    }

    @GetMapping(value = "/xml")
    public String getXmlMenus() {
        return ListMenu.getXmlMenus();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String postMenus() {
        return showSlackMenu();
    }

    @PostMapping(value = "/action", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void postToChannel(String payload) {
        Gson gson = new Gson();
        Payload p = gson.fromJson(payload, Payload.class);

        if (p.actions.length != 1) {
            // log here, this should not be possible
            return;
        }

        String actionValue = p.actions[0].value;

        var client = HttpClient.newHttpClient();

        if ("share_menu".equals(actionValue)) {
            client.sendAsync(shareMessageRequest(p.response_url), HttpResponse.BodyHandlers.ofString());
        }
        else if ("close".equals(actionValue)) {
            client.sendAsync(closeMessageRequest(p.response_url), HttpResponse.BodyHandlers.ofString());
        }
    }

    private HttpRequest closeMessageRequest(URI response_url) {
        Gson gson = new Gson();

        return HttpRequest.newBuilder(response_url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(DeleteMessage.deleteEphemeral())))
                .build();
    }

    private HttpRequest shareMessageRequest(URI response_url) {
        return HttpRequest.newBuilder(response_url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(shareSlackMenu()))
                .build();
    }

    /**
     * POST http://localhost:8080/action
     *
     * {"type":"block_actions","team":{"id":"T9TK3CUKW","domain":"example"},"user":{"id":"UA8RXUSPL","username":"jtorrance","team_id":"T9TK3CUKW"},"api_app_id":"AABA1ABCD","token":"9s8d9as89d8as9d8as989","container":{"type":"message_attachment","message_ts":"1548261231.000200","attachment_id":1,"channel_id":"CBR2V3XEX","is_ephemeral":false,"is_app_unfurl":false},"trigger_id":"12321423423.333649436676.d8c1bb837935619ccad0f624c448ffb3","channel":{"id":"CBR2V3XEX","name":"review-updates"},"message":{"bot_id":"BAH5CA16Z","type":"message","text":"This content can't be displayed.","user":"UAJ2RU415","ts":"1548261231.000200"},"response_url":"https://hooks.slack.com/actions/AABA1ABCD/1232321423432/D09sSasdasdAS9091209","actions":[{"action_id":"WaXA","block_id":"=qXel","text":{"type":"plain_text","text":"View","emoji":true},"value":"click_me_123","type":"button","action_ts":"1548426417.840180"}]}
     */
    class   Payload {
        Channel channel;
        URI response_url;
        Action[] actions;
    }

    class Channel {
        String id;
        String name;
    }
    class Action {
        String value;
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
