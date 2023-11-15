package com.github.sibmaks.ad_vita_bot.exception;

import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
public class ServiceException extends RuntimeException {
    private final ServiceError serviceError;

    public ServiceException(ServiceError serviceError) {
        this.serviceError = serviceError;
    }

    public ServiceException(String message, ServiceError serviceError) {
        super(message);
        this.serviceError = serviceError;
    }

    public ServiceException(String message, Throwable cause, ServiceError serviceError) {
        super(message, cause);
        this.serviceError = serviceError;
    }
}
