package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Participant;

/**
 * @author axothy
 * @since 0.0.1
 */
public interface ParticipantService {
    Participant createParticipant(Long chatId);
    Participant getParticipantByChatId(Long chatId);
    Participant updateParticipant(Participant participant);
    void deleteParticipant(Long chatId);
}
