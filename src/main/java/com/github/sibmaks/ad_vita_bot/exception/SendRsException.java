package com.github.sibmaks.ad_vita_bot.exception;

import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
public class SendRsException extends ServiceException {

    public SendRsException() {
        super(ServiceError.SEND_RS_EXCEPTION);
    }

    public SendRsException(String message) {
        super(message, ServiceError.SEND_RS_EXCEPTION);
    }

    public SendRsException(String message, Throwable cause) {
        super(message, cause, ServiceError.SEND_RS_EXCEPTION);
    }
}
