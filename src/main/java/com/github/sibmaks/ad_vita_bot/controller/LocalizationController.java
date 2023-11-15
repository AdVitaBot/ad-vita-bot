package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rs.GetLocalizationsRs;
import com.github.sibmaks.ad_vita_bot.api.rs.UpdateLocalizationRq;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/localization/")
public class LocalizationController {

    @GetMapping(path = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetLocalizationsRs getLocalizations() {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        return new GetLocalizationsRs();
    }

    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdateLocalizationRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        // просто обновить, если по коду не найдено, то ошибка
    }

}
