package jwei26.chathistory.controller;

import jwei26.chathistory.service.WebhookMessageService;
import jwei26.chathistory.service.SchemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WebhookController.class)
public class WebhookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchemaService schemaService;

    @MockBean
    private WebhookMessageService messageService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testReceiveMessageValidPayload() throws Exception {
        String payload = "{\"message\":{\"date\":1718388618,\"chat\":{\"id\":-1002171886894,\"title\":\"testgroup\",\"type\":\"supergroup\",\"username\":\"testgoupppls\"},\"message_id\":7,\"from\":{\"language_code\":\"en\",\"last_name\":\"ren\",\"id\":5558048223,\"is_bot\":false,\"first_name\":\"hua\",\"username\":\"RenHuaW\"},\"text\":\"你好\"}}";
        when(schemaService.schemaExists(any())).thenReturn(false);

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verify(schemaService, times(1)).createSchema(anyString());
        verify(messageService, times(1)).saveMessage(anyLong(), anyLong(), anyString(), any(), anyString(), anyString());
    }

    @Test
    void testReceiveMessageInvalidJson() throws Exception {
        String payload = "{\"invalid_json\": }";

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}

