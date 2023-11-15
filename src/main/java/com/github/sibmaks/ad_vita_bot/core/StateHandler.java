package com.github.sibmaks.ad_vita_bot.core;

import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface StateHandler {

    UserFlowState getHandledState();

    Transition onEnter(long chatId, DefaultAbsSender sender, Update update);

    default Transition onInput(long chatId, DefaultAbsSender sender, Update update) {
        throw new UnsupportedOperationException("Transition doesn't support input");
    }

    default void hideKeyboard(long chatId, DefaultAbsSender sender, CallbackQuery callbackQuery, Logger log) {
        var message = callbackQuery.getMessage();
        var command = buildHideKeyboard(chatId, message.getMessageId());
        try {
            log.debug("[{}] Hide keyboard", chatId);
            sender.execute(command);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            throw new SendRsException("Message sending error",e);
        }
    }

    @NotNull
    private EditMessageReplyMarkup buildHideKeyboard(long chatId, Integer messageId) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(null)
                .build();
    }

}
