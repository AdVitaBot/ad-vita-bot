package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Participant;
import com.github.sibmaks.ad_vita_bot.repos.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author axothy
 * @since 0.0.1
 */
@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public Participant createParticipant(Long chatId) {
        Participant participant = new Participant();
        participant.setChatId(chatId);

        return participantRepository.save(participant);
    }

    @Override
    public Participant getParticipantByChatId(Long chatId) {
        return participantRepository.findById(chatId).get();
    }

    @Override
    public Participant updateParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    public void deleteParticipant(Long chatId) {
        Participant participant = getParticipantByChatId(chatId);
        participantRepository.delete(participant);
    }
}
