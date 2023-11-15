package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.BotParameterEntity;
import com.github.sibmaks.ad_vita_bot.entity.Theme;
import com.github.sibmaks.ad_vita_bot.repository.BotParametersRepository;
import com.github.sibmaks.ad_vita_bot.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class TelegramBotStorage {
    private final BotParametersRepository botParametersRepository;
    private final Map<String, BotParameterEntity> cachedParameters;
    private final ThemeRepository themeRepository;

    public TelegramBotStorage(BotParametersRepository botParametersRepository,
                              ThemeRepository themeRepository) {
        this.botParametersRepository = botParametersRepository;
        this.themeRepository = themeRepository;
        this.cachedParameters = new ConcurrentHashMap<>();
    }


    /**
     * Get invoice provider token
     *
     * @return invoice provider token
     */
    public String getInvoiceProviderToken() {
        var providerToken = cachedParameters.computeIfAbsent("invoice_provider_token", this::loadByCode);
        return providerToken.getValue();
    }

    /**
     * Get last day of receiving donations
     *
     * @return last day of donations receiving
     */
    public LocalDate getDeactivationDate() {
        var deactivationDate = cachedParameters.computeIfAbsent("deactivation_date", this::loadByCode);
        return LocalDate.parse(deactivationDate.getValue(), DateTimeFormatter.ISO_DATE);
    }

    private BotParameterEntity loadByCode(String code) {
        return botParametersRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Bot parameters '%s' doesn't exists".formatted(code)));
    }

    /**
     * Get available themes
     *
     * @return themes list
     */
    public List<Theme> getThemes() {
        return themeRepository.getAllBy();
    }

    public Theme findThemeById(long themeId) {
        return themeRepository.findById(themeId)
                .orElse(null);
    }
}
