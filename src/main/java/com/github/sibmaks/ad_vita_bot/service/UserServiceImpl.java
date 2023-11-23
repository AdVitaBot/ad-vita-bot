package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import com.github.sibmaks.ad_vita_bot.entity.User;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findUser(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public void changePassword(long userId, String oldPassword, String newPassword) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("Пользователь '%d' не найден".formatted(userId), ServiceError.UNEXPECTED_ERROR));

        if(user.getPassword() != null && !BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new ServiceException("Старый пароль не совпадает", ServiceError.VALIDATION_ERROR);
        }

        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

        userRepository.save(user);
    }

}
