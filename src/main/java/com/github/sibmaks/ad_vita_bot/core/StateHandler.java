package com.github.sibmaks.ad_vita_bot.core;

import com.github.sibmaks.ad_vita_bot.dto.UserFlowState;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface StateHandler {

    UserFlowState getHandledState();

    Transition onEnter(long chatId, DefaultAbsSender sender, Update update);

    default Transition onInput(long chatId, DefaultAbsSender sender, Update update) {
        throw new UnsupportedOperationException("Transition doesn't support input");
    }

}
