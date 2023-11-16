package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.UpdateLocalizationRq;
import com.github.sibmaks.ad_vita_bot.auth.Authorized;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
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
@RequestMapping("/api/localization/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocalizationController {
    private final LocalisationService localisationService;

    @Authorized
    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdateLocalizationRq rq) {
        localisationService.update(rq.getCode(), rq.getMessage());
    }

}
