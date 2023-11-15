package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.LoginRq;
import com.github.sibmaks.ad_vita_bot.constant.CommonConst;
import com.github.sibmaks.ad_vita_bot.service.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final SessionService sessionService;

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void login(@RequestBody @Validated LoginRq loginRq, HttpServletResponse response) {
        var sessionId = sessionService.createSession(loginRq.getLogin(), loginRq.getPassword());
        response.setHeader(CommonConst.HEADER_SESSION_ID, sessionId);
    }

    @GetMapping(path = "logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(@RequestHeader(CommonConst.HEADER_SESSION_ID) String sessionId) {
        sessionService.logout(sessionId);
    }
}
