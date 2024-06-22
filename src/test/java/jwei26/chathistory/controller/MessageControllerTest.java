package jwei26.chathistory.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jwei26.chathistory.service.MessageService;
import jwei26.chathistory.service.SchemaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchemaService schemaService;

    @MockBean
    private MessageService messageService;

    @Test
    void testGetEndDateNotFound() throws Exception {
        long chatId = 12345L;
        when(messageService.getLastUpdate(chatId)).thenReturn(null);

        mockMvc.perform(get("/getEndDate")
                        .param("chat_id", String.valueOf(chatId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReceiveMessage() throws Exception {
        String payload = "{\"chat_id\":12345,\"group_name\":\"testGroup\",\"message_id\":67890,\"user_id\":123,\"username\":\"testuser\",\"first_name\":\"Test\",\"last_name\":\"User\",\"text_content\":\"Hello, world!\",\"date\":\"2024-03-20T10:10:00\",\"message_url\":\"http://example.com/message\"}";
        when(schemaService.schemaExists(anyString())).thenReturn(false);

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Received"));

        verify(schemaService).createSchema(anyString());
        verify(schemaService).insertGroupInfo(anyLong(), anyString(), anyString());
        verify(messageService).saveMessage(anyLong(), anyLong(), any(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString());
    }
}
