package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.constant.CommonConst;
import com.github.sibmaks.ad_vita_bot.dto.session.Session;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class SessionServiceImpl implements SessionService {
    private final UserService userService;
    private final Cache<String, Session> sessionCache;

    public SessionServiceImpl(UserService userService,
                              @Value("${app.bot.session.ttl:3600}") long ttl) {
        this.userService = userService;
        this.sessionCache = CacheBuilder.newBuilder()
                .expireAfterAccess(ttl, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String createSession(String username, String password) {
        var user = userService.findUser(username.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect username"));
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        var sessionId = UUID.randomUUID().toString();
        var session = Session.builder()
                .sessionId(sessionId)
                .userId(user.getId())
                .authorized(true)
                .build();
        sessionCache.put(sessionId, session);
        return sessionId;
    }

    @Override
    public boolean isAuthorized(String sessionId) {
        return Optional.ofNullable(sessionCache.getIfPresent(sessionId))
                .map(Session::isAuthorized)
                .orElse(Boolean.FALSE);
    }

    @Override
    public void logout(String sessionId) {
        sessionCache.invalidate(sessionId);
    }

    @Override
    public Session getSession(HttpServletRequest request) {
        var sessionId = request.getHeader(CommonConst.HEADER_SESSION_ID);
        if (sessionId == null && request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (CommonConst.HEADER_SESSION_ID.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if (sessionId != null) {
            return sessionCache.getIfPresent(sessionId);
        }
        return null;
    }
}
