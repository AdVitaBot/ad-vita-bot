package com.github.sibmaks.ad_vita_bot.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.bot.TelegramBotService;
import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.dto.InvoicePayload;
import com.github.sibmaks.ad_vita_bot.entity.Donation;
import com.github.sibmaks.ad_vita_bot.entity.DonationStatus;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.service.DonationService;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

import java.io.ByteArrayInputStream;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentStateMutator implements StateHandler {

    private final TelegramBotService telegramBotService;
    private final ObjectMapper objectMapper;
    private final LocalisationService localisationService;
    private final DonationService donationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.PAYMENT;
    }

    @Override
    public Transition onEnter(long chatId, Update update) {
        return Transition.stop();
    }

    @Override
    public Transition onInput(long chatId, Update update) {
        if (!update.hasMessage()) {
            return Transition.stop();
        }
        var message = update.getMessage();
        if (!message.hasSuccessfulPayment()) {
            return Transition.stop();
        }
        var successfulPayment = message.getSuccessfulPayment();
        var donation = updateDonation(successfulPayment);

        var themeMessage = buildThemeMessage(chatId, donation);
        telegramBotService.sendSync(chatId, "theme", themeMessage);

        var imageMessage = buildImageMessage(chatId, donation);
        telegramBotService.sendSync(chatId, "image", imageMessage);

        return Transition.go(UserFlowState.TRY_MORE);

    }

    private SendMessage buildThemeMessage(Long chatId, Donation donation) {
        var drawing = donation.getDrawing();
        var theme = drawing.getTheme();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("theme_%d_message_text".formatted(theme.getId())))
                .build();
    }

    private SendPhoto buildImageMessage(Long chatId, Donation donation) {
        var drawing = donation.getDrawing();
        var image = new ByteArrayInputStream(drawing.getImage());

        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(image, drawing.getCaption()))
                .build();
    }

    private InvoicePayload readPayload(String text) {
        try {
            return objectMapper.readValue(text, InvoicePayload.class);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Can't read invoice payload", e, ServiceError.UNEXPECTED_ERROR);
        }
    }

    private Donation updateDonation(SuccessfulPayment successfulPayment) {
        var orderInfo = successfulPayment.getOrderInfo();
        var name = orderInfo.getName();
        var email = orderInfo.getEmail();

        var invoicePayload = readPayload(successfulPayment.getInvoicePayload());

        var donation = donationService.getDonationById(invoicePayload.getDonationId());
        donation.setDonatorName(name);
        donation.setDonatorEmail(email);
        donation.setStatus(DonationStatus.SUCCESSFUL);
        return donationService.updateDonation(donation);
    }
}
