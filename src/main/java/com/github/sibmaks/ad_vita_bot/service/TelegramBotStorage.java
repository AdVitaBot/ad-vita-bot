package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.dto.Theme;
import com.github.sibmaks.ad_vita_bot.entity.BotParameterEntity;
import com.github.sibmaks.ad_vita_bot.repository.BotParametersRepository;
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
    private final LocalisationService localisationService;
    private final BotParametersRepository botParametersRepository;
    private final Map<String, BotParameterEntity> cachedParameters;

    public TelegramBotStorage(LocalisationService localisationService,
                              BotParametersRepository botParametersRepository) {
        this.localisationService = localisationService;
        this.botParametersRepository = botParametersRepository;
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
        return List.of(
                new Theme(1, localisationService.getLocalization("theme_1_name")),
                new Theme(2, localisationService.getLocalization("theme_2_name"))
        );
    }

    /**
     * Min charity amount in kopecks
     * {@see https://core.telegram.org/bots/payments#supported-currencies}
     * @return min charity
     */
    public int getMinAmount() {
        return 100_00;
    }


    /**
     * Max charity amount in kopecks
     * {@see https://core.telegram.org/bots/payments#supported-currencies}
     * @return max charity
     */
    public int getMaxAmount() {
        return 900_000_00;
    }

    public Theme findThemeById(int themeId) {
        return getThemes().stream()
                .filter(it -> it.getId() == themeId)
                .findFirst()
                .orElse(null);
    }
}
