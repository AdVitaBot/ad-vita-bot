package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.bot.TelegramBotService;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChooseAmountStateMutator implements StateHandler {
    private static final String OTHER_AMOUNT = "other";

    private final ChatStorage chatStorage;
    private final TelegramBotService telegramBotService;
    private final LocalisationService localisationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.CHOOSE_AMOUNT;
    }

    @Override
    public Transition onEnter(long chatId, Update update) {
        var command = buildEnterMessage(chatId);
        telegramBotService.sendSync(chatId, "chose amount message", command);
        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, Update update) {
        if (!update.hasCallbackQuery()) {
            return Transition.stop();
        }
        var callbackQuery = update.getCallbackQuery();
        var data = callbackQuery.getData();

        hideKeyboard(chatId, telegramBotService, callbackQuery);

        if (OTHER_AMOUNT.equals(data)) {
            return Transition.go(UserFlowState.INPUT_AMOUNT);
        } else {
            var amount = new BigDecimal(data);
            chatStorage.setAmount(chatId, amount);
            return Transition.go(UserFlowState.INVOICE);
        }
    }

    @NotNull
    private SendMessage buildEnterMessage(Long chatId) {
        var keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                                inlineKeyboardButton("100 ₽", "100"),
                                inlineKeyboardButton("200 ₽", "200"),
                                inlineKeyboardButton("300 ₽", "300")
                        )
                )
                .keyboardRow(List.of(
                                inlineKeyboardButton("500 ₽", "500"),
                                inlineKeyboardButton("1 000 ₽", "1000"),
                                inlineKeyboardButton("2 000 ₽", "2000")
                        )
                )
                .keyboardRow(List.of(
                                inlineKeyboardButton(localisationService.getLocalization("other_amount_text"), OTHER_AMOUNT)
                        )
                )
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("choose_amount_text"))
                .replyMarkup(keyboard)
                .build();
    }

    public InlineKeyboardButton inlineKeyboardButton(String text, String value) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(value);
        return button;
    }

}
