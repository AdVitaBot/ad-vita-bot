package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.dto.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

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
            throw new SendRsException("Message sending error",e);
        }

        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, DefaultAbsSender sender, Update update) {
        if (!update.hasCallbackQuery()) {
            return Transition.stop();
        }
        var callbackQuery = update.getCallbackQuery();
        hideKeyboard(chatId, sender, callbackQuery, log);
        return Transition.go(UserFlowState.CHOOSE_THEME);
    }

    @NotNull
    private SendMessage buildEnterMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("thankfully_message_text"))
                .replyMarkup(replyKeyboard())
                .build();
    }

    private InlineKeyboardMarkup replyKeyboard() {
        var tryMoreButton = localisationService.getLocalization("try_more_button_text");
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        inlineKeyboardButton(tryMoreButton, "repeat")
                ))
                .build();
    }

    public InlineKeyboardButton inlineKeyboardButton(String text, String value) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(value);
        return button;
    }

}
