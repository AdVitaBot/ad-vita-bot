package com.github.sibmaks.ad_vita_bot.core;

import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;

import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface Transition {

    /**
     * Compare transition and passed states
     *
     * @param state state to compare
     * @return true - states are the same, false - otherwise
     */
    boolean isDifferent(UserFlowState state);

    /**
     * Next user flow state, can be null in case if state not changed
     * @return next user flow state, nullable
     */
    UserFlowState getNextState();

    /**
     * Factory method for stop transition - stay on current state.
     *
     * @return stop transition instance
     */
    static Transition stop() {
        return new TransitionImpl(null);
    }

    /**
     * Factory method for creating transition on next state.
     *
     * @param nextState next state
     * @return next transition instance
     */
    static Transition go(UserFlowState nextState) {
        Objects.requireNonNull(nextState, "Next state must be not null");
        return new TransitionImpl(nextState);
    }
}
