package com.github.sibmaks.ad_vita_bot.repository;

import com.github.sibmaks.ad_vita_bot.entity.Donation;
import com.github.sibmaks.ad_vita_bot.entity.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DonationRepository extends CrudRepository<Donation, Long> {
    Page<Donation> findDonationByParticipant(Participant participant, Pageable pageable);
    List<Donation> findDonationByParticipant(Participant participant);
}
