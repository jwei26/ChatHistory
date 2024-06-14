package jwei26.chathistory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSchemaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createSchemaAndTables(String schemaName) {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\"");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".users (user_id BIGINT PRIMARY KEY, username VARCHAR(255), first_name VARCHAR(255), last_name VARCHAR(255))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".messages (message_id BIGINT PRIMARY KEY, user_id BIGINT REFERENCES \"" + schemaName + "\".users(user_id), date_time TIMESTAMP, content TEXT, message_url TEXT)");
    }
}

