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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InputAmountStateMutator implements StateHandler {
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^([0-9])+(.[0-9]{1,2})?$");

    private final ChatStorage chatStorage;
    private final TelegramBotService telegramBotService;
    private final LocalisationService localisationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.INPUT_AMOUNT;
    }

    @Override
    public Transition onEnter(long chatId, Update update) {
        var command = buildEnterMessage(chatId);
        telegramBotService.sendSync(chatId, "input amount message", command);
        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, Update update) {
        if (!update.hasMessage()) {
            return Transition.stop();
        }
        var message = update.getMessage();
        if (!message.hasText()) {
            return Transition.stop();
        }
        var text = message.getText();
        var amount = tryParseAmount(text);
        if (amount != null && isValid(chatId, amount)) {
            chatStorage.setAmount(chatId, amount);
            return Transition.go(UserFlowState.INVOICE);
        } else {
            var command = buildErrorMessage(chatId);
            telegramBotService.sendSync(chatId, "incorrect amount message", command);
            return Transition.stop();
        }
    }

    private boolean isValid(long chatId, BigDecimal amount) {
        var theme = chatStorage.getTheme(chatId);
        var minAmount = theme.getMinDonationAmount();
        var maxAmount = theme.getMaxDonationAmount();
        return amount.compareTo(minAmount) >= 0 && amount.compareTo(maxAmount) <= 0;
    }

    private BigDecimal tryParseAmount(String text) {
        try {
            if(!AMOUNT_PATTERN.matcher(text).matches()) {
                return null;
            }
            return new BigDecimal(text);
        } catch (Exception ignored) {
            return null;
        }
    }

    @NotNull
    private SendMessage buildEnterMessage(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("input_amount_text"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    @NotNull
    private SendMessage buildErrorMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("input_amount_error_text"))
                .build();
    }
}
