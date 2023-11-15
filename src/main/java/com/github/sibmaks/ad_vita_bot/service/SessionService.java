package com.github.sibmaks.ad_vita_bot.service;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface SessionService {

    String createSession(String username, String password);

    boolean isActive(String sessionId);

    void logout(String sessionId);
}
