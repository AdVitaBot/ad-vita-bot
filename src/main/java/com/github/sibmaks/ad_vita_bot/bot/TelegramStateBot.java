package com.github.sibmaks.ad_vita_bot.bot;

import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Telegram state bot
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class TelegramStateBot extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    private final UserFlowState initialFlowState;
    private final ChatStorage chatStorage;
    private final TelegramBotStorage telegramBotStorage;
    private final ChatIdSupplier chatIdSupplier;
    private final LocalisationService localisationService;
    private final Map<UserFlowState, StateHandler> transitionHandlers;

    public TelegramStateBot(DefaultBotOptions defaultBotOptions,
                            TelegramBotProperties telegramBotProperties,
                            ChatStorage chatStorage,
                            TelegramBotStorage telegramBotStorage,
                            ChatIdSupplier chatIdSupplier,
                            LocalisationService localisationService,
                            List<StateHandler> stateHandlers) {
        super(defaultBotOptions, telegramBotProperties.getToken());
        this.botUsername = telegramBotProperties.getName();
        this.initialFlowState = telegramBotProperties.getInitialFlowState();
        this.chatStorage = chatStorage;
        this.telegramBotStorage = telegramBotStorage;
        this.chatIdSupplier = chatIdSupplier;
        this.localisationService = localisationService;
        this.transitionHandlers = stateHandlers.stream()
                .collect(Collectors.toMap(StateHandler::getHandledState, Function.identity()));
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = chatIdSupplier.getChatId(update);
        if (chatId == null) {
            log.warn("Can't determine chatId for update {}", update.getUpdateId());
            return;
        }
        try {
            if (preCheckDate(chatId)) {
                return;
            }
            var state = chatStorage.getState(chatId);
            if (state != null) {
                if (update.hasMessage()) {
                    var message = update.getMessage();
                    if (message.hasText() && "/start".equals(message.getText())) {
                        state = null;
                    }
                }
            }
            Transition transition;
            if (state == null) {
                state = initialFlowState;
                var transitionHandler = getHandler(state);
                transition = transitionHandler.onEnter(chatId, this, update);
            } else {
                var transitionHandler = getHandler(state);
                transition = transitionHandler.onInput(chatId, this, update);
            }
            state = proceedTransition(update, chatId, state, transition);
            chatStorage.setState(chatId, state);
        } catch (ServiceException e) {
            log.error("[%s] Technical issue happened, code: %s".formatted(chatId, e.getServiceError()), e);
            sendTechnicalIssueMessage(chatId);
        } catch (Exception e) {
            log.error("[%s] Unexpected exception happened", e);
            sendTechnicalIssueMessage(chatId);
        }
    }

    private void sendTechnicalIssueMessage(Long chatId) {
        var command = buildTechnicalErrorMessage(chatId);
        try {
            log.warn("[{}] Send technical issue message", chatId);
            execute(command);
        } catch (TelegramApiException inner) {
            log.error("Last hope error", inner);
        }
    }

    private boolean preCheckDate(Long chatId) {
        var deactivationDate = telegramBotStorage.getDeactivationDate();
        if (!LocalDate.now().isAfter(deactivationDate)) {
            return false;
        }
        var command = buildGoodByeMessage(chatId);
        try {
            log.debug("[{}] Send goodbye message", chatId);
            execute(command);
        } catch (TelegramApiException e) {
            log.error("Message sending error", e);
            throw new SendRsException("Message sending error", e);
        }
        return true;
    }

    private SendMessage buildGoodByeMessage(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("goodbye_message"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    private SendMessage buildTechnicalErrorMessage(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("technical_error_text"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    /**
     * Proceed transition until stop
     *
     * @param update     incoming update
     * @param chatId     chat identifier
     * @param state      current flow state
     * @param transition next transition
     * @return final flow state
     */
    private UserFlowState proceedTransition(Update update,
                                            Long chatId,
                                            UserFlowState state,
                                            Transition transition) {
        while (transition.isDifferent(state)) {
            var nextState = transition.getNextState();
            var transitionHandler = getHandler(nextState);
            transition = transitionHandler.onEnter(chatId, this, update);
            state = nextState;
        }
        return state;
    }

    /**
     * Get handler for user state
     *
     * @param state user state
     * @return transition handler or exception
     */
    private StateHandler getHandler(UserFlowState state) {
        var transitionHandler = transitionHandlers.get(state);
        if (transitionHandler == null) {
            throw new IllegalStateException("There are no handlers for state %s".formatted(state));
        }
        return transitionHandler;
    }
}
