package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Theme;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author axothy
 * @since 0.0.1
 */
public interface ThemeService {
    List<Theme> getAllThemes();

    Theme getById(Long themeId);

    Theme createTheme(BigDecimal minDonationAmount, BigDecimal maxDonationAmount, String description);

    Theme createTheme(BigDecimal maxDonationAmount, String description);

    void deleteTheme(Long themeId);

    void updateTheme(Long id, String description, BigDecimal minDonationAmount, BigDecimal maxDonationAmount);
}
