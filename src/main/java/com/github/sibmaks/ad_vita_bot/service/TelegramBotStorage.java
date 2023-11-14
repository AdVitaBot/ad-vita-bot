package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.dto.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TelegramBotStorage {
    private final LocalisationService localisationService;


    /**
     * Get invoice provider token
     *
     * @return invoice provider token
     */
    public String getInvoiceProviderToken() {
        return "381764678:TEST:71235";
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
