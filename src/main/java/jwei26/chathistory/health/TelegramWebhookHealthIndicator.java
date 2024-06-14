package jwei26.chathistory.health;

import jwei26.chathistory.service.TelegramHealthService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramWebhookHealthIndicator implements HealthIndicator {

    @Autowired
    private TelegramHealthService telegramHealthService;

    @Override
    public Health health() {
        if (telegramHealthService.checkWebhook()) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("reason", "Telegram Webhook configuration error or recent errors").build();
        }
    }
}

