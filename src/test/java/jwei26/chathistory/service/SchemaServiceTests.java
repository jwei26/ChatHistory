package jwei26.chathistory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchemaServiceTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SchemaService schemaService;

    @Test
    public void testCreateSchema() {
        String schemaName = "test_schema";
        schemaService.createSchema(schemaName);

        verify(jdbcTemplate, times(1)).execute(eq("CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\""));
        verify(jdbcTemplate, times(1)).execute(contains("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".users"));
        verify(jdbcTemplate, times(1)).execute(contains("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".messages"));
    }

    @Test
    public void testSchemaExists() {
        String schemaName = "test_schema";
        when(jdbcTemplate.queryForList(any(String.class), any(Object[].class), eq(String.class)))
                .thenReturn(Collections.singletonList(schemaName));

        boolean exists = schemaService.schemaExists(schemaName);

        verify(jdbcTemplate, times(1)).queryForList(any(String.class), any(Object[].class), eq(String.class));
        assert(exists);
    }

    @Test
    public void testInsertGroupInfo() {
        long groupId = 1L;
        String groupName = "Test Group";
        String schemaName = "test_schema";

        when(jdbcTemplate.queryForList(any(String.class), any(Object[].class), eq(String.class)))
                .thenReturn(Collections.emptyList());

        schemaService.insertGroupInfo(groupId, groupName, schemaName);

        verify(jdbcTemplate, times(1)).update(any(String.class), eq(groupId), eq(groupName), eq(schemaName));
    }
}

