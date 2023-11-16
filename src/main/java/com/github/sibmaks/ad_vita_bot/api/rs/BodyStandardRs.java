package com.github.sibmaks.ad_vita_bot.api.rs;

import com.github.sibmaks.ad_vita_bot.api.ErrorRs;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Getter
@Setter
public class BodyStandardRs<T> extends StandardRs {
    private T body;

    public BodyStandardRs(T body) {
        this.body = body;
    }

    public BodyStandardRs(ErrorRs errorRs) {
        super(errorRs);
    }
}
