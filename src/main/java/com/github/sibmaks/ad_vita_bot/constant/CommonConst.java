package com.github.sibmaks.ad_vita_bot.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonConst {
    public static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);
    public static final String HEADER_SESSION_ID = "X-Session-Id";
}
