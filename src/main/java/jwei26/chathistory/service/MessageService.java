package jwei26.chathistory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class MessageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveMessage(long messageId, long userId, String content, Timestamp dateTime, String messageUrl, String schemaName) {
        String sql = String.format("INSERT INTO \"%s\".messages (message_id, user_id, content, date_time, message_url) VALUES (?, ?, ?, ?, ?)", schemaName);
        jdbcTemplate.update(sql, messageId, userId, content, dateTime, messageUrl);
    }

    public void saveUser(long userId, String username, String firstName, String lastName, String schemaName) {
        String sql = String.format("INSERT INTO \"%s\".users (user_id, username, first_name, last_name) VALUES (?, ?, ?, ?) ON CONFLICT (user_id) DO NOTHING", schemaName);
        jdbcTemplate.update(sql, userId, username, firstName, lastName);
    }
}
