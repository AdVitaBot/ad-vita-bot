package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.ErrorRs;
import com.github.sibmaks.ad_vita_bot.api.rs.StandardRs;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public void handleUnauthorized(UnauthorizedException e) {
        log.error("Unauthorized access", e);
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(ServiceException.class)
    public StandardRs handleServiceException(ServiceException e) {
        log.error("Service exception", e);
        var serviceError = e.getServiceError();
        var errorRs = new ErrorRs(serviceError.name(), e.getMessage());
        return new StandardRs(errorRs);
    }

}
