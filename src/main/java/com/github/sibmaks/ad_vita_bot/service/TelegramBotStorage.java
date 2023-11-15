package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.BotParameterEntity;
import com.github.sibmaks.ad_vita_bot.entity.Theme;
import com.github.sibmaks.ad_vita_bot.repository.BotParametersRepository;
import com.github.sibmaks.ad_vita_bot.repository.ThemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class TelegramBotStorage {
    private static final String INVOICE_PROVIDER_TOKEN = "invoice_provider_token";
    private static final String DEACTIVATION_DATE = "deactivation_date";

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
        var providerToken = cachedParameters.computeIfAbsent(INVOICE_PROVIDER_TOKEN, this::loadByCode);
        return providerToken.getValue();
    }

    /**
     * Get last day of receiving donations
     *
     * @return last day of donations receiving
     */
    public LocalDate getDeactivationDate() {
        var deactivationDate = cachedParameters.computeIfAbsent(DEACTIVATION_DATE, this::loadByCode);
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

    @Transactional
    public void setSettings(String invoiceProviderToken, LocalDate deactivationDate) {
        botParametersRepository.findById(INVOICE_PROVIDER_TOKEN)
                        .ifPresent(it -> {
                            if(!Objects.equals(it.getValue(), invoiceProviderToken)) {
                                it.setValue(invoiceProviderToken);
                                botParametersRepository.save(it);
                            }
                        });
        var deactivationDateSerialized = Optional.ofNullable(deactivationDate)
                        .map(it -> it.format(DateTimeFormatter.ISO_DATE))
                                .orElse(null);
        botParametersRepository.findById(DEACTIVATION_DATE)
                .ifPresent(it -> {
                    if(!Objects.equals(it.getValue(), deactivationDateSerialized)) {
                        it.setValue(deactivationDateSerialized);
                        botParametersRepository.save(it);
                    }
                });
        cachedParameters.clear();

    }
}
