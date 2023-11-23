package com.github.sibmaks.ad_vita_bot.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.dto.InvoicePayload;
import com.github.sibmaks.ad_vita_bot.entity.Donation;
import com.github.sibmaks.ad_vita_bot.entity.DonationStatus;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.repository.DrawingRepository;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.DonationService;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.OrderInfo;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

import static com.github.sibmaks.ad_vita_bot.constant.CommonConst.HUNDRED;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceStateMutator implements StateHandler {

    private final ChatStorage chatStorage;
    private final ObjectMapper objectMapper;
    private final TelegramBotStorage telegramBotStorage;
    private final LocalisationService localisationService;
    private final DonationService donationService;
    private final DrawingRepository drawingRepository;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.INVOICE;
    }

    @Override
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var theme = chatStorage.getTheme(chatId);
        var drawing = drawingRepository.findLeastUsed(theme.getId(), chatId);
        if(drawing == null) {
            throw new ServiceException("No drawings in theme %d".formatted(theme.getId()), ServiceError.UNEXPECTED_ERROR);
        }
        var amount = chatStorage.getAmount(chatId);
        var donation = donationService.createDonation(chatId, amount, drawing);

        var command = buildEnterMessage(chatId, donation);

        try {
            log.info("[{}] Send invoice", chatId);
            sender.execute(command);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            throw new SendRsException("Message sending error", e);
        }

        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, DefaultAbsSender sender, Update update) {
        if (!update.hasPreCheckoutQuery()) {
            return Transition.stop();
        }
        var preCheckoutQuery = update.getPreCheckoutQuery();
        updateDonation(preCheckoutQuery);

        var query = new AnswerPreCheckoutQuery();
        query.setOk(true);
        query.setPreCheckoutQueryId(preCheckoutQuery.getId());
        try {
            log.info("[{}] Send ok answer on pre checkout query", chatId);
            sender.execute(query);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            throw new SendRsException("Message sending error", e);
        }
        return Transition.go(UserFlowState.PAYMENT);
    }

    private void updateDonation(PreCheckoutQuery preCheckoutQuery) {
        var orderInfo = Optional.ofNullable(preCheckoutQuery.getOrderInfo());
        var name = orderInfo.map(OrderInfo::getName)
                        .orElse(null);
        var email = orderInfo.map(OrderInfo::getEmail)
                .orElse(null);

        var invoicePayload = readPayload(preCheckoutQuery.getInvoicePayload());

        var donation = donationService.getDonationById(invoicePayload.getDonationId());
        donation.setDonatorName(name);
        donation.setDonatorEmail(email);
        donation.setStatus(DonationStatus.PRE_CHECK_QUERY);
        donationService.updateDonation(donation);
    }

    @NotNull
    private SendInvoice buildEnterMessage(Long chatId, Donation donation) {
        var invoicePayload = InvoicePayload.builder()
                .chatId(chatId)
                .donationId(donation.getId())
                .build();

        var amount = chatStorage.getAmount(chatId);
        var invoiceProviderToken = telegramBotStorage.getInvoiceProviderToken();
        var kopecksAmount = amount.multiply(HUNDRED).intValue();

        var payload = toPayload(invoicePayload);
        return SendInvoice.builder()
                .chatId(chatId)
                .title(localisationService.getLocalization("invoice_title"))
                .description(localisationService.getLocalization("invoice_description"))
                .startParameter("ad_vita_bot")
                .payload(payload)
                .price(new LabeledPrice(localisationService.getLocalization("invoice_price_label"), kopecksAmount))
                .currency("RUB")
                .needName(Boolean.TRUE)
                .needEmail(Boolean.TRUE)
                .sendEmailToProvider(Boolean.TRUE)
                .providerToken(invoiceProviderToken)
                .replyMarkup(paymentKeyboard())
                .build();
    }

    private InlineKeyboardMarkup paymentKeyboard() {
        var payButton = localisationService.getLocalization("pay_button_text");
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        payKeyboardButton(payButton)
                ))
                .build();
    }

    public InlineKeyboardButton payKeyboardButton(String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .pay(Boolean.TRUE)
                .build();
    }

    private String toPayload(InvoicePayload invoicePayload) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(invoicePayload);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Unexpected invoice payload creation exception", e, ServiceError.UNEXPECTED_ERROR);
        }
        return payload;
    }

    private InvoicePayload readPayload(String text) {
        try {
            return objectMapper.readValue(text, InvoicePayload.class);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Can't read invoice payload", e, ServiceError.UNEXPECTED_ERROR);
        }
    }

}
