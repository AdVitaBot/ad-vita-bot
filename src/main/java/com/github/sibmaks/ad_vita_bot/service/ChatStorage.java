package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import com.github.sibmaks.ad_vita_bot.entity.Participant;
import com.github.sibmaks.ad_vita_bot.entity.Theme;
import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class ChatStorage {
    private final LoadingCache<Long, Participant> participants;
    private final ParticipantService participantService;

    public ChatStorage(ParticipantService participantService,
                       TelegramBotProperties telegramBotProperties) {
        this.participantService = participantService;
        var initialFlowState = telegramBotProperties.getInitialFlowState();
        var loader = buildParticipantLoader(participantService, initialFlowState);
        participants = CacheBuilder.newBuilder()
                .maximumSize(telegramBotProperties.getMaxCachedParticipants())
                .build(loader);
    }

    private static CacheLoader<Long, Participant> buildParticipantLoader(ParticipantService participantService,
                                                                         UserFlowState initialFlowState) {
        return new CacheLoader<>() {
            @NotNull
            @Override
            public Participant load(@NotNull Long key) {
                return participantService.getOrCreateParticipant(key, initialFlowState);
            }
        };
    }

    /**
     * Get current chat state
     *
     * @param chatId chat identifier
     * @return user flow state
     */
    public UserFlowState getState(long chatId) {
        var participant = tryGetParticipant(chatId);
        return participant.getState();
    }

    @NotNull
    private Participant tryGetParticipant(long chatId) {
        try {
            return participants.get(chatId);
        } catch (ExecutionException e) {
            throw new ServiceException("Can't load participant", e, ServiceError.UNEXPECTED_ERROR);
        }
    }

    /**
     * Set current chat state
     *
     * @param chatId chat identifier
     * @param state new chat state
     */
    public void setState(long chatId, UserFlowState state) {
        var participant = tryGetParticipant(chatId);
        if (participant.getState() == state) {
            return;
        }
        participant.setState(state);
        participant = participantService.updateParticipant(participant);
        participants.put(chatId, participant);
    }

    /**
     * Get chat chosen theme
     *
     * @param chatId chat identifier
     * @return chat chosen theme
     */
    public Theme getTheme(long chatId) {
        var participant = tryGetParticipant(chatId);
        return participant.getTheme();
    }

    /**
     * Set chat chosen theme
     *
     * @param chatId chat identifier
     * @param theme chat chosen theme
     */
    public void setTheme(long chatId, Theme theme) {
        var participant = tryGetParticipant(chatId);
        participant.setTheme(theme);
        participant = participantService.updateParticipant(participant);
        participants.put(chatId, participant);
    }

    /**
     * Get chat charity amount
     *
     * @param chatId chat identifier
     * @return chat charity amount
     */
    public BigDecimal getAmount(long chatId) {
        var participant = tryGetParticipant(chatId);
        return participant.getAmount();
    }


    /**
     * Set chat charity amount
     *
     * @param chatId chat identifier
     * @param amount chat charity amount
     */
    public void setAmount(long chatId, BigDecimal amount) {
        var participant = tryGetParticipant(chatId);
        participant.setAmount(amount);
        participant = participantService.updateParticipant(participant);
        participants.put(chatId, participant);
    }

}
