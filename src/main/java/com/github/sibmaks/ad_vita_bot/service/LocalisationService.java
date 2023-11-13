package com.github.sibmaks.ad_vita_bot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.entity.Localization;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class LocalisationService {
    private final Map<String, String> defaultLocalizations;

    @SneakyThrows
    public LocalisationService(@Value("classpath:i18n/ru.json")
                               Resource defaultLocalizations,
                               ObjectMapper objectMapper) {
        var localizations = objectMapper.readValue(defaultLocalizations.getInputStream(), Localization[].class);
        this.defaultLocalizations = Arrays.stream(localizations)
                .collect(Collectors.toMap(Localization::getCode, Localization::getMessage));
    }

    /**
     * Get localization by code
     *
     * @param code localization code
     * @return localized message
     */
    public String getLocalization(String code) {
        return defaultLocalizations.get(code);
    }

}
