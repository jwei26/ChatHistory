package jwei26.chathistory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseSchemaServiceTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DatabaseSchemaService databaseSchemaService;

    @Test
    public void testCreateSchemaAndTables() {
        String schemaName = "test_schema";

        databaseSchemaService.createSchemaAndTables(schemaName);

        verify(jdbcTemplate).execute("CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\"");
        verify(jdbcTemplate).execute("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".users (user_id BIGINT PRIMARY KEY, username VARCHAR(255), first_name VARCHAR(255), last_name VARCHAR(255))");
        verify(jdbcTemplate).execute("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".messages (message_id BIGINT PRIMARY KEY, user_id BIGINT REFERENCES \"" + schemaName + "\".users(user_id), date_time TIMESTAMP, content TEXT, message_url TEXT)");
    }
}

