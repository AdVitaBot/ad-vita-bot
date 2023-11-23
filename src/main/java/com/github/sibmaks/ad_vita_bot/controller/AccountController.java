package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.ChangePasswordRq;
import com.github.sibmaks.ad_vita_bot.auth.Authorized;
import com.github.sibmaks.ad_vita_bot.service.SessionService;
import com.github.sibmaks.ad_vita_bot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
 * @since 0.0.5
 */
@RestController
@RequestMapping("/api/account/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {
    private final UserService userService;
    private final SessionService sessionService;

    @PostMapping(path = "changePassword", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorized
    public void changePassword(@RequestBody @Validated ChangePasswordRq loginRq, HttpServletRequest request) {
        var session = sessionService.getSession(request);
        userService.changePassword(session.getUserId(), loginRq.getOldPassword(), loginRq.getNewPassword());
    }

}
