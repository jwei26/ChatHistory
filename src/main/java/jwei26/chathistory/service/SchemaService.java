package jwei26.chathistory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SchemaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createSchema(String schemaName) {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\"");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".users (" +
                "user_id BIGINT PRIMARY KEY, " +
                "username VARCHAR(255), " +
                "first_name VARCHAR(255), " +
                "last_name VARCHAR(255))");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".messages (" +
                "message_id BIGINT PRIMARY KEY, " +
                "user_id BIGINT REFERENCES \"" + schemaName + "\".users(user_id), " +
                "date_time TIMESTAMP, " +
                "content TEXT, " +
                "message_url TEXT)");
    }

    public boolean schemaExists(String schemaName) {
        String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = ?";
        List<String> schemas = jdbcTemplate.queryForList(sql, new Object[]{schemaName}, String.class);
        return !schemas.isEmpty();
    }

    public void updateGroupLastUpdate(long groupId, Timestamp lastUpdate) {

        String sql = "UPDATE central.groups SET last_update = ? WHERE group_id = ?";

        jdbcTemplate.update(sql, lastUpdate, groupId);

    }

    public void insertGroupInfo(long groupId, String groupName, String schemaName) {
        if (!schemaExists(schemaName)) {
            createSchema(schemaName);
        }
        String sql = "INSERT INTO central.groups (group_id, group_name, data_schema) VALUES (?, ?, ?) ON CONFLICT (group_id) DO NOTHING";
        jdbcTemplate.update(sql, groupId, groupName, schemaName);
        updateGroupLastUpdate(groupId, new Timestamp(System.currentTimeMillis()));
    }
}

