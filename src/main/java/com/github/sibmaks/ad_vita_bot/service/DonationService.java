package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Donation;

import java.util.List;

/**
 * @author axothy
 * @since 0.0.1
 */
public interface DonationService {
    Donation createDonation(Long chatId, int amount);
    Donation getDonationById(Long donationId);
    Donation updateDonation(Donation donation);
    List<Donation> getAllDonationsByChatId(Long chatId);
    void deleteDonation(Long donationId);
}

