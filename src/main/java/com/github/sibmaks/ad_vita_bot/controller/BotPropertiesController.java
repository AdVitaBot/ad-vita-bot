package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.UpdatePropertiesRq;
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
public class BotPropertiesController {

    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdatePropertiesRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
    }

}
