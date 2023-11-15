package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.LoginRq;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void login(@RequestBody @Validated LoginRq loginRq) {
        // TODO: login logic
        // should send {@link CommonConst#HEADER_SESSION_ID} with session identifier
    }

    @GetMapping(path = "logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout() {
        // TODO: logout logic
        // accept {@link CommonConst#HEADER_SESSION_ID} as header
    }
}
