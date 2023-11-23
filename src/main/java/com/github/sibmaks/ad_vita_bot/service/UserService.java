package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.User;

import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.5
 */
public interface UserService {

    Optional<User> findUser(String login);

    void changePassword(long userId, String oldPassword, String newPassword);
}
