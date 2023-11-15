package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.UpdateThemeRq;
import com.github.sibmaks.ad_vita_bot.auth.Authorized;
import com.github.sibmaks.ad_vita_bot.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/theme/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThemeController {
    private final ThemeService themeService;

    @Authorized
    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdateThemeRq rq) {
        themeService.updateTheme(rq.getId(), rq.getDescription(), rq.getMinDonationAmount(), rq.getMaxDonationAmount());
    }

}
