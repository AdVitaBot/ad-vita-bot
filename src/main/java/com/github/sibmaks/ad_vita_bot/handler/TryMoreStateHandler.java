package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TryMoreStateHandler implements StateHandler {
    private final LocalisationService localisationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.TRY_MORE;
    }

    @Override
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var command = buildEnterMessage(chatId);

        try {
            log.debug("[{}] Send try more message", chatId);
            sender.execute(command);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            // TODO: retry on error?
        }

        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, DefaultAbsSender sender, Update update) {
        if (!update.hasMessage()) {
            return Transition.stop();
        }
        var message = update.getMessage();
        if (!message.hasText()) {
            return Transition.stop();
        }
        return Transition.go(UserFlowState.CHOOSE_THEME);
    }

    @NotNull
    private SendMessage buildEnterMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("try_more_text"))
                .replyMarkup(tryMoreKeyboard())
                .build();
    }

    private ReplyKeyboard tryMoreKeyboard() {
        var tryMoreButton = localisationService.getLocalization("try_more_button_text");
        var keyboardRow = new KeyboardRow(1);
        keyboardRow.add(tryMoreButton);
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .resizeKeyboard(Boolean.TRUE)
                .build();
    }
}
