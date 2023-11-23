package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.dto.session.Session;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface SessionService {

    String createSession(String username, String password);

    boolean isAuthorized(String sessionId);

    void logout(String sessionId);

    /**
     * Get session identifier from http request.
     * Looking for session id in headers and cookies
     *
     * @param request http servlet request
     * @return session identifier
     */
    Session getSession(HttpServletRequest request);
}
