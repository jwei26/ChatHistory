package jwei26.chathistory.service;


import jwei26.chathistory.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Timestamp;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTestsV2 {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testSaveMessageWithNullUser() {
        long chatId = 12345;
        long messageId = 54321;
        Long userId = null;
        String username = null;
        String firstName = null;
        String lastName = null;
        String content = "Test message";
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        String messageUrl = "http://example.com/message";
        String schemaName = "test_schema";

        messageService.saveMessage(chatId, messageId, userId, username, firstName, lastName, content, dateTime, messageUrl, schemaName);

        verify(jdbcTemplate).update(anyString(), eq(messageId), eq(null), eq(content), eq(dateTime), eq(messageUrl));
    }
}
