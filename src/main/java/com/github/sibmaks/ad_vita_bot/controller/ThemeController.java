package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.GetThemeDrawingsRq;
import com.github.sibmaks.ad_vita_bot.api.rs.GetThemeDrawingsRs;
import com.github.sibmaks.ad_vita_bot.api.rs.GetThemesRs;
import com.github.sibmaks.ad_vita_bot.api.rq.UpdateThemeRq;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/theme/")
public class ThemeController {

    @GetMapping(path = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetThemesRs getThemes() {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        return new GetThemesRs();
    }

    @PostMapping(path = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Validated UpdateThemeRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
    }

    @PostMapping(path = "drawings", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetThemeDrawingsRs getDrawings(@RequestBody @Validated GetThemeDrawingsRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        return new GetThemeDrawingsRs();
    }
}
