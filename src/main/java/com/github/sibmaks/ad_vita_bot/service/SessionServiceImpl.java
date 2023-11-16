package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.conf.AdminProperties;
import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class SessionServiceImpl implements SessionService {
    private final String username;
    private final String hashedPassword;
    private final Cache<String, String> sessionCache;

    public SessionServiceImpl(TelegramBotProperties telegramBotProperties,
                              @Value("${app.bot.session.ttl:3600}") long ttl) {
        var admin = telegramBotProperties.getAdmin();
        this.username = admin.getUsername();
        this.hashedPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
        this.sessionCache = CacheBuilder.newBuilder()
                .expireAfterAccess(ttl, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String createSession(String username, String password) {
        if(!this.username.equalsIgnoreCase(username)) {
            throw new IllegalArgumentException("Incorrect username");
        }
        if(!BCrypt.checkpw(password, this.hashedPassword)) {
            throw new IllegalArgumentException("Incorrect password");
        }

        var sessionId = UUID.randomUUID().toString();
        sessionCache.put(sessionId, sessionId);
        return sessionId;
    }

    @Override
    public boolean isActive(String sessionId) {
        return sessionCache.getIfPresent(sessionId) != null;
    }

    @Override
    public void logout(String sessionId) {
        sessionCache.invalidate(sessionId);
    }
}
