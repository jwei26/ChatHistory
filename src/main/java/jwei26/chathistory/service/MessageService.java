package jwei26.chathistory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Service
public class MessageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveMessage(long chatId, long messageId, Long userId, String username, String firstName, String lastName, String content, Timestamp dateTime, String messageUrl, String schemaName) {
        if (userId != null) {
            saveUser(userId, username, firstName, lastName, schemaName);
        }
        saveMessageData(chatId, messageId, userId, content, dateTime, messageUrl, schemaName);
    }

    private void saveMessageData(long chatId, long messageId, Long userId, String content, Timestamp dateTime, String messageUrl, String schemaName) {
        String sql = String.format("INSERT INTO \"%s\".messages (message_id, user_id, content, date_time, message_url) VALUES (?, ?, ?, ?, ?)", schemaName);
        jdbcTemplate.update(sql, messageId, userId, content, dateTime, messageUrl);
        jdbcTemplate.update("UPDATE central.groups SET last_update = NOW() WHERE group_id = ?", chatId);
    }

    private void saveUser(long userId, String username, String firstName, String lastName, String schemaName) {
        String sql = String.format("INSERT INTO \"%s\".users (user_id, username, first_name, last_name) VALUES (?, ?, ?, ?) ON CONFLICT (user_id) DO NOTHING", schemaName);
        jdbcTemplate.update(sql, userId, username, firstName, lastName);
    }

    public Timestamp getLastUpdate(long chatId){
        String sql = "SELECT last_update FROM central.groups WHERE group_id = ?";
        Timestamp lastUpdate = jdbcTemplate.queryForObject(sql, new Object[]{chatId}, Timestamp.class);
        return lastUpdate;
    }
}

