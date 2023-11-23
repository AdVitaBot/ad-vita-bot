package com.github.sibmaks.ad_vita_bot.auth;

import com.github.sibmaks.ad_vita_bot.constant.CommonConst;
import com.github.sibmaks.ad_vita_bot.exception.UnauthorizedException;
import com.github.sibmaks.ad_vita_bot.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationService {
    private final SessionService sessionService;

    @Around("@annotation(Authorized)")
    public Object audit(ProceedingJoinPoint pjp) throws Throwable {
        var request = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        if(request == null) {
            throw new UnauthorizedException("No request scope for authorization check");
        }
        var header = request.getHeader(CommonConst.HEADER_SESSION_ID);
        if(header == null && request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if(CommonConst.HEADER_SESSION_ID.equals(cookie.getName())) {
                    header = cookie.getValue();
                    break;
                }
            }
        }
        if(header == null) {
            throw new UnauthorizedException("Session header/cookie not presented");
        }
        if(!sessionService.isAuthorized(header)) {
            throw new UnauthorizedException("Session not active");
        }
        return pjp.proceed(pjp.getArgs());
    }
}
