package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Theme;
import com.github.sibmaks.ad_vita_bot.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.github.sibmaks.ad_vita_bot.constant.CommonConst.HUNDRED;

/**
 * @author axothy
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThemeServiceImpl implements ThemeService {
    private final ThemeRepository themeRepository;

    @Override
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    @Override
    public Theme getById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme %s doesn't exists".formatted(themeId)));
    }

    @Override
    public Theme createTheme(BigDecimal minDonationAmount, BigDecimal maxDonationAmount, String description) {
        var theme = new Theme();
        theme.setMinDonationAmount(minDonationAmount);
        theme.setMaxDonationAmount(maxDonationAmount);
        theme.setDescription(description);

        return themeRepository.save(theme);
    }

    @Override
    public Theme createTheme(BigDecimal maxDonationAmount, String description) {
        return createTheme(HUNDRED, maxDonationAmount, description);
    }

    @Override
    public void deleteTheme(Long themeId) {
        Theme theme = getById(themeId);
        themeRepository.delete(theme);
    }

    @Override
    public void updateTheme(Long id, String description, BigDecimal minDonationAmount, BigDecimal maxDonationAmount) {
        var theme = getById(id);
        theme.setDescription(description);
        theme.setMinDonationAmount(minDonationAmount);
        theme.setMaxDonationAmount(maxDonationAmount);
        themeRepository.save(theme);
    }
}
