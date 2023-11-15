package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.UpdatePropertiesRq;
import com.github.sibmaks.ad_vita_bot.api.rs.GetPropertiesRs;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/properties/")
public class BotPropertiesController {

    @GetMapping(path = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetPropertiesRs getProperties() {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        return new GetPropertiesRs();
    }

    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdatePropertiesRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
    }

}
