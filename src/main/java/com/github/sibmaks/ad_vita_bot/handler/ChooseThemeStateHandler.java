package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
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

import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChooseThemeStateHandler implements StateHandler {

    private final ChatStorage chatStorage;
    private final TelegramBotStorage telegramBotStorage;
    private final LocalisationService localisationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.CHOOSE_THEME;
    }

    @Override
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var command = buildEnterMessage(chatId);

        try {
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
        var text = message.getText();
        var themes = telegramBotStorage.getThemes();
        if (themes.contains(text)) {
            chatStorage.setTheme(chatId, text);
            return Transition.go(UserFlowState.INPUT_AMOUNT);
        } else {
            var sendPhoto = buildErrorMessage(chatId);

            try {
                sender.execute(sendPhoto);
            } catch (TelegramApiException e) {
                log.error("Message sending error", e);
                // TODO: retry on error?
            }

            return Transition.stop();
        }
    }

    @NotNull
    private SendMessage buildEnterMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("pick_one_theme_text"))
                .replyMarkup(replyKeyboard())
                .build();
    }

    @NotNull
    private SendMessage buildErrorMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("pick_exists_theme_text"))
                .replyMarkup(replyKeyboard())
                .build();
    }

    private ReplyKeyboard replyKeyboard() {
        var themes = telegramBotStorage.getThemes();
        var keyboardRows = themes.stream()
                .map(this::keyboardRow)
                .collect(Collectors.toList());
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    private KeyboardRow keyboardRow(String button) {
        var keyboardRow = new KeyboardRow(1);
        keyboardRow.add(button);
        return keyboardRow;
    }
}
