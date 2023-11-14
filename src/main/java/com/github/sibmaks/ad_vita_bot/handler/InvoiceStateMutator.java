package com.github.sibmaks.ad_vita_bot.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.dto.InvoicePayload;
import com.github.sibmaks.ad_vita_bot.dto.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    private final LocalisationService localisationService;


    @SneakyThrows
    public InvoiceStateMutator(@Value("classpath:apple.png") Resource demoResource,
                               ChatStorage chatStorage,
                               ObjectMapper objectMapper,
                               TelegramBotStorage telegramBotStorage,
                               LocalisationService localisationService) {
        this.demoResource = demoResource;
        this.chatStorage = chatStorage;
        this.objectMapper = objectMapper;
        this.telegramBotStorage = telegramBotStorage;
        this.localisationService = localisationService;
    }

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.INVOICE;
    }

    @Override
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var command = buildEnterMessage(chatId);

        try {
            log.debug("[{}] Send invoice", chatId);
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
                log.debug("[{}] Send ok answer on pre checkout query", chatId);
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
                var thankfullyMessage = buildThankfullyMessage(chatId);
                try {
                    log.debug("[{}] Send thankfully message", chatId);
                    sender.execute(thankfullyMessage);
                } catch (TelegramApiException e) {
                    log.error("Message sending error", e);
                    // TODO: retry on error?
                }

                var themeMessage = buildThemeMessage(chatId);
                try {
                    log.debug("[{}] Send theme message", chatId);
                    sender.execute(themeMessage);
                } catch (TelegramApiException e) {
                    log.error("Message sending error", e);
                    // TODO: retry on error?
                }

                var imageMessage = buildImageMessage(chatId);
                try {
                    log.debug("[{}] Send image", chatId);
                    sender.execute(imageMessage);
                } catch (TelegramApiException e) {
                    log.error("Message sending error", e);
                    // TODO: retry on error?
                }
                return Transition.go(UserFlowState.TRY_MORE);
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

        var amount = new BigDecimal(chatStorage.getAmount(chatId));
        var invoiceProviderToken = telegramBotStorage.getInvoiceProviderToken();
        var kopecksAmount = amount.multiply(HUNDRED).intValue();

        return SendInvoice.builder()
                .chatId(chatId)
                .title(localisationService.getLocalization("invoice_title"))
                .description(localisationService.getLocalization("invoice_description"))
                .startParameter("")
                .payload(objectMapper.writeValueAsString(invoicePayload))
                .price(new LabeledPrice(localisationService.getLocalization("invoice_price_label"), kopecksAmount))
                .currency("RUB")
                .needEmail(Boolean.TRUE)
                .sendEmailToProvider(Boolean.TRUE)
                .providerToken(invoiceProviderToken)
                .build();
    }

    @SneakyThrows
    private SendMessage buildThankfullyMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("thankfully_message_text"))
                .build();
    }

    @SneakyThrows
    private SendMessage buildThemeMessage(Long chatId) {
        var theme = chatStorage.getTheme(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("theme_%d_message_text".formatted(theme.getId())))
                .build();
    }

    @SneakyThrows
    private SendPhoto buildImageMessage(Long chatId) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(demoResource.getInputStream(), "Картинка"))
                .build();
    }
}
