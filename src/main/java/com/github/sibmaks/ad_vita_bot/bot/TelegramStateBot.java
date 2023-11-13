package com.github.sibmaks.ad_vita_bot.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.entity.InvoicePayload;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    private final ObjectMapper objectMapper;
    private final Map<UserFlowState, StateHandler> transitionHandlers;

    public TelegramStateBot(DefaultBotOptions defaultBotOptions,
                            TelegramBotProperties telegramBotProperties,
                            ChatStorage chatStorage,
                            ObjectMapper objectMapper,
                            List<StateHandler> stateHandlers) {
        super(defaultBotOptions, telegramBotProperties.getToken());
        this.botUsername = telegramBotProperties.getName();
        this.initialFlowState = telegramBotProperties.getInitialFlowState();
        this.chatStorage = chatStorage;
        this.objectMapper = objectMapper;
        this.transitionHandlers = stateHandlers.stream()
                .collect(Collectors.toMap(StateHandler::getHandledState, Function.identity()));
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = getChatId(update);
        if(chatId == null) {
            log.warn("Can't determine chatId for update {}", update.getUpdateId());
            return;
        }
        var state = chatStorage.getState(chatId);
        Transition transition;
        if(state == null) {
            state = initialFlowState;
            var transitionHandler = getHandler(state);
            transition = transitionHandler.onEnter(chatId, this, update);
        } else {
            var transitionHandler = getHandler(state);
            transition = transitionHandler.onInput(chatId, this,  update);
        }
        state = proceedTransition(update, chatId, state, transition);
        chatStorage.setState(chatId, state);
    }

    /**
     * Proceed transition until stop
     *
     * @param update incoming update
     * @param chatId chat identifier
     * @param state current flow state
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
        if(transitionHandler == null) {
            throw new IllegalStateException("There are no handlers for state %s".formatted(state));
        }
        return transitionHandler;
    }

    /**
     * Find chat identifier in incoming update
     *
     * @param update incoming update
     * @return chat id or null
     */
    private Long getChatId(Update update) {
        if(update.hasMessage()) {
            var message = update.getMessage();
            return message.getChatId();
        } else if(update.hasPreCheckoutQuery()) {
            var preCheckoutQuery = update.getPreCheckoutQuery();
            // XXX: Dirty staff
            var payloadJson = preCheckoutQuery.getInvoicePayload();
            try {
                var payload = objectMapper.readValue(payloadJson, InvoicePayload.class);
                return payload.getChatId();
            } catch (JsonProcessingException e) {
                log.error("Invalid invoice payload", e);
                throw new RuntimeException(e);
            }
        } else {
            log.warn("Unsupported update happened: {}", update.getUpdateId());
        }
        return null;
    }
}
