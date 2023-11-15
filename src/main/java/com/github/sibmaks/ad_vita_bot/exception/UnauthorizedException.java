package com.github.sibmaks.ad_vita_bot.exception;

import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
public class UnauthorizedException extends ServiceException {

    public UnauthorizedException() {
        super(ServiceError.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(message, ServiceError.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause, ServiceError.UNAUTHORIZED);
    }
}
