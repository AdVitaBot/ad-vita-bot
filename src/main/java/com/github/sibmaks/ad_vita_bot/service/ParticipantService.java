package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Participant;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;

/**
 * @author axothy
 * @since 0.0.1
 */
public interface ParticipantService {
    Participant createParticipant(Long chatId, UserFlowState initialState);

    Participant getParticipantByChatId(Long chatId);

    Participant getOrCreateParticipant(Long chatId, UserFlowState initialState);

    Participant updateParticipant(Participant participant);

    void deleteParticipant(Long chatId);
}
