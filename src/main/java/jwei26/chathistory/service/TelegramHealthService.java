package jwei26.chathistory.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramHealthService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean checkWebhook() {
        String url = "https://api.telegram.org/bot" + botToken + "/getWebhookInfo";
        try {
            WebhookInfo response = restTemplate.getForObject(url, WebhookInfo.class);
            return response != null;
        } catch (Exception e) {
            return false;
        }
    }
}
