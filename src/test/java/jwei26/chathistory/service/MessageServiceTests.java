package jwei26.chathistory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private WebhookMessageService messageService;

    @Test
    public void testSaveMessage() {
        long messageId = 1L;
        long userId = 1L;
        String content = "Hello, World!";
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        String messageUrl = "http://example.com/message";
        String schemaName = "test_schema";

        messageService.saveMessage(messageId, userId, content, dateTime, messageUrl, schemaName);

        String expectedSql = String.format("INSERT INTO \"%s\".messages (message_id, user_id, content, date_time, message_url) VALUES (?, ?, ?, ?, ?)", schemaName);
        verify(jdbcTemplate).update(eq(expectedSql), eq(messageId), eq(userId), eq(content), eq(dateTime), eq(messageUrl));
    }

    @Test
    public void testSaveUser() {
        long userId = 1L;
        String username = "johndoe";
        String firstName = "John";
        String lastName = "Doe";
        String schemaName = "test_schema";

        messageService.saveUser(userId, username, firstName, lastName, schemaName);

        String expectedSql = String.format("INSERT INTO \"%s\".users (user_id, username, first_name, last_name) VALUES (?, ?, ?, ?) ON CONFLICT (user_id) DO NOTHING", schemaName);
        verify(jdbcTemplate).update(eq(expectedSql), eq(userId), eq(username), eq(firstName), eq(lastName));
    }
}

