package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    private static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^([0-9])+(.[0-9]{1,2})?$");

    private final ChatStorage chatStorage;
    private final TelegramBotStorage telegramBotStorage;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.INPUT_AMOUNT;
    }

    @Override
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var command = buildEnterMessage(chatId);

        try {
            log.debug("[{}] Send input amount message", chatId);
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
        if (isValid(text)) {
            chatStorage.setAmount(chatId, text);
            return Transition.go(UserFlowState.INVOICE);
        } else {
            var command = buildErrorMessage(chatId);

            try {
                log.warn("[{}] Send incorrect amount message", chatId);
                sender.execute(command);
            } catch (TelegramApiException e) {
                log.error("Message sending error", e);
                // TODO: retry on error?
            }

            return Transition.stop();
        }
    }

    private boolean isValid(String text) {
        try {
            if(!AMOUNT_PATTERN.matcher(text).matches()) {
                return false;
            }

            var amount = new BigDecimal(text);
            var minAmount = telegramBotStorage.getMinAmount();
            var maxAmount = telegramBotStorage.getMaxAmount();

            var amountKopecks = amount.multiply(HUNDRED).intValue();

            return amountKopecks >= minAmount && amountKopecks <= maxAmount;
        } catch (Exception e) {
            return false;
        }
    }

    @NotNull
    private SendMessage buildEnterMessage(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Введите сумму пожертвования в рублях в формате 0.00. Минимальная сумма пожертвования 100₽")
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    @NotNull
    private SendMessage buildErrorMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Укажите корректную сумму пожертвования")
                .build();
    }
}
