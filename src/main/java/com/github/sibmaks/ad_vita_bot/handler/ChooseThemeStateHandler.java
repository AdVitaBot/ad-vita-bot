package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.dto.UserFlowState;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
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
            log.debug("[{}] Send chose theme message", chatId);
            var rs = sender.execute(command);
            log.debug("[{}] Rs was sent: {}", chatId, rs.getMessageId());
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            // TODO: retry on error?
        }

        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, DefaultAbsSender sender, Update update) {
        if (!update.hasCallbackQuery()) {
            return Transition.stop();
        }
        var callbackQuery = update.getCallbackQuery();
        // TODO: вместо текста использовать идентификатор
        var themeId = Integer.parseInt(callbackQuery.getData());
        var theme = telegramBotStorage.findThemeById(themeId);
        if (theme != null) {
            chatStorage.setTheme(chatId, theme);
            var message = callbackQuery.getMessage();
            var command = buildHideKeyboard(chatId, message.getMessageId());
            try {
                log.debug("[{}] Hide keyboard", chatId);
                sender.execute(command);
            } catch (TelegramApiException e) {
                log.error("Message sending error", e);
                // TODO: retry on error?
            }

            return Transition.go(UserFlowState.CHOOSE_AMOUNT);
        } else {
            var command = buildErrorMessage(chatId);

            try {
                log.warn("[{}] Send unknown theme chosen", chatId);
                var rs = sender.execute(command);
                log.debug("[{}] Rs was sent: {}", chatId, rs.getMessageId());
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

    @NotNull
    private EditMessageReplyMarkup buildHideKeyboard(long chatId, Integer messageId) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(null)
                .build();
    }

    private InlineKeyboardMarkup replyKeyboard() {
        var themes = telegramBotStorage.getThemes();
        var keyboardRows = themes.stream()
                .map(it -> List.of(inlineKeyboardButton(it.getText(), String.valueOf(it.getId()))))
                .collect(Collectors.toList());
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .keyboardRow(List.of(
                        urlInlineKeyboardButton(
                                localisationService.getLocalization("about_fund_text"),
                                localisationService.getLocalization("fund_url")
                        )
                ))
                .build();
    }

    public InlineKeyboardButton inlineKeyboardButton(String text, String value) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(value);
        return button;
    }

    public InlineKeyboardButton urlInlineKeyboardButton(String text, String url) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        return button;
    }
}
