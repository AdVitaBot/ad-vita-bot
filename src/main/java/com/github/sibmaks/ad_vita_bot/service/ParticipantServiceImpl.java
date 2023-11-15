package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Participant;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author axothy
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;

    @Override
    public Participant createParticipant(Long chatId, UserFlowState initialState) {
        var participant = new Participant();
        participant.setChatId(chatId);
        participant.setState(initialState);

        return participantRepository.save(participant);
    }

    @Override
    public Participant getParticipantByChatId(Long chatId) {
        return participantRepository.findById(chatId).get();
    }

    @Override
    public Participant getOrCreateParticipant(Long chatId, UserFlowState initialState) {
        return participantRepository.findById(chatId)
                .orElseGet(() -> createParticipant(chatId, initialState));
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
