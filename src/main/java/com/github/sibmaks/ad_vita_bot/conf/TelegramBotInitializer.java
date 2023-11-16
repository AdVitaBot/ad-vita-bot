package com.github.sibmaks.ad_vita_bot.conf;

import com.github.sibmaks.ad_vita_bot.bot.TelegramStateBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TelegramBotInitializer {
    private final TelegramStateBot telegramStateBot;
    private final TelegramBotProperties telegramBotProperties;

    @EventListener(ContextRefreshedEvent.class)
    public void init() throws TelegramApiException {
        if(!telegramBotProperties.isEnabled()) {
            return;
        }
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramStateBot);
        } catch (TelegramApiException e) {
            log.error("Bot initialization error", e);
        }
    }

}
