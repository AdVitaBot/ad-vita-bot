package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Donation;
import com.github.sibmaks.ad_vita_bot.entity.DonationStatus;
import com.github.sibmaks.ad_vita_bot.entity.Participant;
import com.github.sibmaks.ad_vita_bot.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author axothy
 * @since 0.0.1
 */
@Service
public class DonationServiceImpl implements DonationService {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private DonationRepository donationRepository;

    @Override
    public Donation createDonation(Long chatId, int amount) {
        Participant participant = participantService.getParticipantByChatId(chatId);

        if (participant == null) {
            return null;
        }

        Donation donation = new Donation();
        donation.setDonationDate(LocalDateTime.now());
        donation.setStatus(DonationStatus.AWAITING_PAYMENT);
        donation.setAmount(amount);
        donation.setParticipant(participant);

        return donationRepository.save(donation);
    }

    @Override
    public Donation getDonationById(Long donationId) {
        return donationRepository.findById(donationId).get();
    }

    @Override
    public Donation updateDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    @Override
    public List<Donation> getAllDonationsByChatId(Long chatId) {
        Participant participant = participantService.getParticipantByChatId(chatId);

        if (participant == null) {
            return null;
        }

        return donationRepository.findDonationByParticipant(participant);
    }

    @Override
    public void deleteDonation(Long donationId) {
        Donation donation = getDonationById(donationId);
        donationRepository.delete(donation);
    }
}
