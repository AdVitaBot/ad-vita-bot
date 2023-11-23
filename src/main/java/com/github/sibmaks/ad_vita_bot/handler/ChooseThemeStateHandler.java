package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.bot.TelegramBotService;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
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
    private final TelegramBotService telegramBotService;
    private final TelegramBotStorage telegramBotStorage;
    private final LocalisationService localisationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.CHOOSE_THEME;
    }

    @Override
    public Transition onEnter(long chatId, Update update) {
        var command = buildEnterMessage(chatId);
        telegramBotService.sendSync(chatId, "chose theme message", command);
        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, Update update) {
        if (!update.hasCallbackQuery()) {
            return Transition.stop();
        }
        var callbackQuery = update.getCallbackQuery();
        var themeId = Integer.parseInt(callbackQuery.getData());
        var theme = telegramBotStorage.findThemeById(themeId);
        if (theme != null) {
            chatStorage.setTheme(chatId, theme);
            hideKeyboard(chatId, telegramBotService, callbackQuery);

            return Transition.go(UserFlowState.CHOOSE_AMOUNT);
        } else {
            var command = buildErrorMessage(chatId);

            telegramBotService.sendSync(chatId, "unknown theme", command);

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

    private InlineKeyboardMarkup replyKeyboard() {
        var themes = telegramBotStorage.getThemes();
        var keyboardRows = themes.stream()
                .map(it -> List.of(inlineKeyboardButton(it.getDescription(), String.valueOf(it.getId()))))
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
