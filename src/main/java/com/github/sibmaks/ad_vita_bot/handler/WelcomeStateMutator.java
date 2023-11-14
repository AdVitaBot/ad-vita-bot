package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class WelcomeStateMutator implements StateHandler {

    private final Resource logoResource;
    private final LocalisationService localisationService;


    @SneakyThrows
    public WelcomeStateMutator(@Value("classpath:logo.png") Resource logoResource,
                               LocalisationService localisationService) {
        this.logoResource = logoResource;
        this.localisationService = localisationService;
    }

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.WELCOME;
    }

    @Override
    @SneakyThrows
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var sendPhoto = buildSendPhoto(chatId);

        try {
            log.debug("[{}] Send welcome message", chatId);
            sender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            // TODO: retry on error?
        }

        return Transition.go(UserFlowState.CHOOSE_THEME);
    }

    @NotNull
    private SendPhoto buildSendPhoto(Long chatId) throws IOException {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendPhoto.builder()
                .chatId(chatId)
                .caption(localisationService.getLocalization("welcome_text"))
                // TODO: took from another place
                .photo(new InputFile(logoResource.getInputStream(), "AdVita!"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }
}
