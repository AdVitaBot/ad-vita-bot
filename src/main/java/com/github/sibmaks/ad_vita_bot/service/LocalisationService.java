package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.LocalizationEntity;
import com.github.sibmaks.ad_vita_bot.repository.LocalizationRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
public class LocalisationService {
    private final Map<String, String> cachedLocalizations;
    private final LocalizationRepository localizationRepository;

    public LocalisationService(LocalizationRepository localizationRepository) {
        this.localizationRepository = localizationRepository;
        this.cachedLocalizations = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        log.debug("Start localization loading");
        for (var localizationEntity : localizationRepository.findAll()) {
            cachedLocalizations.put(localizationEntity.getCode(), localizationEntity.getMessage());
        }
        log.debug("Localization loaded");
    }

    /**
     * Get localization by code
     *
     * @param code localization code
     * @return localized message
     */
    public String getLocalization(String code) {
        return cachedLocalizations.computeIfAbsent(code, this::loadLocalization);
    }

    private String loadLocalization(String code) {
        return localizationRepository.findById(code)
                .map(LocalizationEntity::getMessage)
                .orElseThrow(() -> new IllegalArgumentException("Localization %s does not exists".formatted(code)));
    }

}
