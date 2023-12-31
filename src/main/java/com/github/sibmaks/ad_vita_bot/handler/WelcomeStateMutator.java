package com.github.sibmaks.ad_vita_bot.handler;

import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
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

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WelcomeStateMutator implements StateHandler {

    private final LocalisationService localisationService;

    @Override
    public UserFlowState getHandledState() {
        return UserFlowState.WELCOME;
    }

    @Override
    public Transition onEnter(long chatId, DefaultAbsSender sender, Update update) {
        var sendPhoto = buildSendPhoto(chatId);

        try {
            log.debug("[{}] Send welcome message", chatId);
            sender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            throw new SendRsException("Message sending error",e);
        }

        return Transition.go(UserFlowState.CHOOSE_THEME);
    }

    @NotNull
    private SendMessage buildSendPhoto(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("welcome_text"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }
}
