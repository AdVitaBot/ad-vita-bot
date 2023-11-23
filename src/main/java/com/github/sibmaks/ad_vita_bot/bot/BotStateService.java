package com.github.sibmaks.ad_vita_bot.bot;

import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.core.StateHandler;
import com.github.sibmaks.ad_vita_bot.core.Transition;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Slf4j
@Service
public class BotStateService {
    private final UserFlowState initialFlowState;
    private final ChatStorage chatStorage;
    private final Map<UserFlowState, StateHandler> transitionHandlers;

    public BotStateService(TelegramBotProperties telegramBotProperties,
                           ChatStorage chatStorage,
                           List<StateHandler> stateHandlers) {
        this.initialFlowState = telegramBotProperties.getInitialFlowState();
        this.chatStorage = chatStorage;
        this.transitionHandlers = stateHandlers.stream()
                .collect(Collectors.toMap(StateHandler::getHandledState, Function.identity()));
    }

    public void update(long chatId, Update update) {
        var state = chatStorage.getState(chatId);
        if (state != null && update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasText() && "/start".equals(message.getText())) {
                state = null;
            }
        }
        Transition transition;
        if (state == null) {
            state = initialFlowState;
            var transitionHandler = getHandler(state);
            transition = transitionHandler.onEnter(chatId, update);
        } else {
            var transitionHandler = getHandler(state);
            transition = transitionHandler.onInput(chatId, update);
        }
        state = proceedTransition(update, chatId, state, transition);
        chatStorage.setState(chatId, state);
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
            transition = transitionHandler.onEnter(chatId, update);
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
