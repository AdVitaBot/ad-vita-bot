package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.LoginRq;
import com.github.sibmaks.ad_vita_bot.api.rs.LoginRs;
import com.github.sibmaks.ad_vita_bot.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginRs login(@RequestBody @Validated LoginRq loginRq) {
        // should send {@link CommonConst#HEADER_SESSION_ID} with session identifier

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRq.getLogin(),
                        loginRq.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return new LoginRs(token);
    }

    @GetMapping(path = "logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout() {
        // TODO: logout logic
        // accept {@link CommonConst#HEADER_SESSION_ID} as header
    }
}
