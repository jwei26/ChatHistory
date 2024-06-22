package jwei26.chathistory.controller;

import jwei26.chathistory.service.MessageService;
import jwei26.chathistory.service.SchemaService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class MessageController {

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/getEndDate")
    public ResponseEntity<?> getEndDate(@RequestParam("chat_id") long chatId) {
        try {
            Timestamp lastUpdate = messageService.getLastUpdate(chatId);

            if (lastUpdate == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(Map.of("endDate", lastUpdate.toString()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Request failed: " + e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<String> receiveMessage(@RequestBody String payload) {
        try {
            JSONObject json = new JSONObject(payload);
            long chatId = json.getLong("chat_id");
            String groupName = json.getString("group_name");
            long messageId = json.getLong("message_id");
            Long userId = json.has("user_id") && !json.isNull("user_id") ? json.getLong("user_id") : null;
            String username = json.optString("username", null);
            String firstName = json.optString("first_name", null);
            String lastName = json.optString("last_name", null);
            String content = json.optString("text_content", "");
            Timestamp dateTime = convertStringToTimestamp(json.getString("date"));
            String messageUrl = json.optString("message_url", null);
            String schemaName = formatSchemaName(groupName, chatId);

            if (!schemaService.schemaExists(schemaName)) {
                schemaService.createSchema(schemaName);
                schemaService.insertGroupInfo(chatId, groupName, schemaName);
            }

            messageService.saveMessage(chatId, messageId, userId, username, firstName, lastName, content, dateTime, messageUrl, schemaName);
            return ResponseEntity.ok("Received");
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body("Invalid data");
        }
    }

    private String formatSchemaName(String groupName, long id) {
        String safeGroupName = groupName.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "_");
        return "group_" + id + "_" + safeGroupName;
    }

    public Timestamp convertStringToTimestamp(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
        return Timestamp.valueOf(dateTime);
    }
}
