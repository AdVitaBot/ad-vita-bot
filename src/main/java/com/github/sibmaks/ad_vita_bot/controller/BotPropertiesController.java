package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.UpdatePropertiesRq;
import com.github.sibmaks.ad_vita_bot.auth.Authorized;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
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
@RequestMapping("/api/properties/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotPropertiesController {
    private final TelegramBotStorage telegramBotStorage;

    @Authorized
    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdatePropertiesRq rq) {
        telegramBotStorage.setSettings(rq.getInvoiceProviderToken(), rq.getDeactivationDate());
    }

}
