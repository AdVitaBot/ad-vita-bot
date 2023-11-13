package com.github.sibmaks.ad_vita_bot.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.InvoicePayload;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class InvoiceStateMutator implements StateHandler {
    private static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);

    private final Resource demoResource;
    private final ChatStorage chatStorage;
    private final ObjectMapper objectMapper;
    private final TelegramBotStorage telegramBotStorage;


    @SneakyThrows
    public InvoiceStateMutator(@Value("classpath:apple.png") Resource demoResource,
                               ChatStorage chatStorage,
                               ObjectMapper objectMapper,
                               TelegramBotStorage telegramBotStorage) {
        this.demoResource = demoResource;
        this.chatStorage = chatStorage;
        this.objectMapper = objectMapper;
        this.telegramBotStorage = telegramBotStorage;
    }

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.INVOICE;
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
        if (update.hasPreCheckoutQuery()) {
            var preCheckoutQuery = update.getPreCheckoutQuery();

            var query = new AnswerPreCheckoutQuery();
            query.setOk(true);
            query.setPreCheckoutQueryId(preCheckoutQuery.getId());
            try {
                sender.execute(query);
            } catch (TelegramApiException e) {
                log.error("Message sending error", e);
                // TODO: retry on error?
            }
        } else if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasSuccessfulPayment()) {
                var successfulPayment = message.getSuccessfulPayment();
                // TODO: random image pick
                var sendPhoto = buildThankfullyMessage(chatId);
                try {
                    sender.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    log.error("Message sending error", e);
                    // TODO: retry on error?
                }
                return Transition.go(UserFlowState.CHOOSE_THEME);
            }
        }

        return Transition.stop();
    }

    @SneakyThrows
    @NotNull
    private SendInvoice buildEnterMessage(Long chatId) {
        var invoicePayload = InvoicePayload.builder()
                .chatId(chatId)
                .build();

        var theme = chatStorage.getTheme(chatId);
        var amount = new BigDecimal(chatStorage.getAmount(chatId));
        var invoiceProviderToken = telegramBotStorage.getInvoiceProviderToken();

        return SendInvoice.builder()
                .chatId(chatId)
                .title("Пожертвование")
                .description("Вы выбрали тему: " + theme)
                .startParameter("")
                .payload(objectMapper.writeValueAsString(invoicePayload))
                .price(new LabeledPrice("Сумма А", amount.multiply(HUNDRED).intValue()))
                .currency("RUB")
                .needEmail(Boolean.TRUE)
                .providerToken(invoiceProviderToken)
                .build();
    }

    @SneakyThrows
    private SendPhoto buildThankfullyMessage(Long chatId) {
        return SendPhoto.builder()
                .chatId(chatId)
                .caption("Спасибо за пожертвование")
                .photo(new InputFile(demoResource.getInputStream(), "Картинка"))
                .build();
    }
}
