package com.github.sibmaks.ad_vita_bot.core;

import com.github.sibmaks.ad_vita_bot.bot.TelegramBotService;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface StateHandler {

    UserFlowState getHandledState();

    Transition onEnter(long chatId, Update update);

    default Transition onInput(long chatId, Update update) {
        throw new UnsupportedOperationException("Transition doesn't support input");
    }

    default void hideKeyboard(long chatId, TelegramBotService service, CallbackQuery callbackQuery) {
        var message = callbackQuery.getMessage();
        var command = buildHideKeyboard(chatId, message.getMessageId());
        service.sendSync(chatId, "hide keyboard", command);
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
