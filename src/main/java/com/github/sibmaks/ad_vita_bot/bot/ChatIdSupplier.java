package com.github.sibmaks.ad_vita_bot.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.dto.InvoicePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatIdSupplier {
    private final ObjectMapper objectMapper;


    /**
     * Find chat identifier in incoming update
     *
     * @param update incoming update
     * @return chat id or null
     */
    public Long getChatId(Update update) {
        if(update.hasMessage()) {
            var message = update.getMessage();
            return message.getChatId();
        } else if(update.hasPreCheckoutQuery()) {
            var preCheckoutQuery = update.getPreCheckoutQuery();
            // XXX: Dirty staff
            var payloadJson = preCheckoutQuery.getInvoicePayload();
            try {
                var payload = objectMapper.readValue(payloadJson, InvoicePayload.class);
                return payload.getChatId();
            } catch (JsonProcessingException e) {
                log.error("Invalid invoice payload", e);
                throw new RuntimeException(e);
            }
        } else if(update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            var message = callbackQuery.getMessage();
            return message.getChatId();
        } else {
            log.warn("Unsupported update happened: {}", update.getUpdateId());
        }
        return null;
    }

}
