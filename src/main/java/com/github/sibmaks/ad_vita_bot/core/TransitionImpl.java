package com.github.sibmaks.ad_vita_bot.core;

import com.github.sibmaks.ad_vita_bot.dto.UserFlowState;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
class TransitionImpl implements Transition {
    private UserFlowState nextState;

    @Override
    public boolean isDifferent(UserFlowState state) {
        return nextState != null && state != nextState;
    }
}
