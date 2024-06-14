package jwei26.chathistory.controller;

import jwei26.chathistory.service.SchemaService;
import jwei26.chathistory.service.MessageService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class WebhookController {

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody String payload) {
        try {
            JSONObject json = new JSONObject(payload);
            processMessage(json);
            return ResponseEntity.ok("Received");
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body("Invalid data");
        }
    }

    private void processMessage(JSONObject json) {
        if (json.has("my_chat_member")) {
            JSONObject myChatMember = json.getJSONObject("my_chat_member");
            JSONObject chat = myChatMember.getJSONObject("chat");
            long groupId = chat.getLong("id");
            String title = chat.getString("title");
            String schemaName = formatSchemaName(title, groupId);
            if (!schemaService.schemaExists(schemaName)) {
                schemaService.createSchema(schemaName);
                schemaService.insertGroupInfo(groupId, title, schemaName);
            }
        } else if (json.has("message")) {
            JSONObject message = json.getJSONObject("message");
            JSONObject chat = message.getJSONObject("chat");
            String type = chat.getString("type");

            if (type.equals("group") || type.equals("supergroup")) {
                long groupId = chat.getLong("id");
                String title = chat.getString("title");
                String schemaName = formatSchemaName(title, groupId);
                if (!schemaService.schemaExists(schemaName)) {
                    schemaService.createSchema(schemaName);
                    schemaService.insertGroupInfo(groupId, title, schemaName);
                }
                storeMessage(message, schemaName);
            }
        }
    }

    private void storeMessage(JSONObject message, String schemaName) {
        long messageId = message.getLong("message_id");
        JSONObject from = message.getJSONObject("from");
        long userId = from.getLong("id");
        String username = from.optString("username", null);
        String firstName = from.optString("first_name", null);
        String lastName = from.optString("last_name", null);
        String content = message.optString("text", null);
        Timestamp dateTime = new Timestamp(message.getLong("date") * 1000L);
        JSONObject chat = message.getJSONObject("chat");
        String messageUrl = generateMessageUrl(chat, messageId);
        messageService.saveUser(userId, username, firstName, lastName, schemaName);
        messageService.saveMessage(messageId, userId, content, dateTime, messageUrl, schemaName);
    }

    private String generateMessageUrl(JSONObject chat, long messageId) {
        String type = chat.getString("type");
        long id = chat.getLong("id");
        String username = chat.optString("username", null);
        Long threadId = null;
        Long commentId = null;
        String mediaTimestamp = null;

        StringBuilder urlBuilder = new StringBuilder();

        if (type.equals("supergroup") || type.equals("group")) {
            if (username != null) {
                urlBuilder.append("https://t.me/");
                urlBuilder.append(username);
                if (threadId != null) {
                    urlBuilder.append("/").append(threadId);
                }
                urlBuilder.append("/").append(messageId);
                if (commentId != null) urlBuilder.append("?comment=").append(commentId);
                if (mediaTimestamp != null) urlBuilder.append("&t=").append(mediaTimestamp);
            } else {
                urlBuilder.append("https://t.me/c/");
                urlBuilder.append(Math.abs(id));
                if (threadId != null) {
                    urlBuilder.append("/").append(threadId);
                }
                urlBuilder.append("/").append(messageId);
                if (commentId != null) urlBuilder.append("?comment=").append(commentId);
                if (mediaTimestamp != null) urlBuilder.append("&t=").append(mediaTimestamp);
            }
        } else {
            return null;
        }

        return urlBuilder.toString();
    }

    private String formatSchemaName(String title, long id) {
        String safeTitle = title.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "_");
        return "group_" + id + "_" + safeTitle;
    }
}


