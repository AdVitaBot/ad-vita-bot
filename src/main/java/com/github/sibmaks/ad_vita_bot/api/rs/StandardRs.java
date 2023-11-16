package com.github.sibmaks.ad_vita_bot.api.rs;

import com.github.sibmaks.ad_vita_bot.api.ErrorRs;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Getter
@Setter
public class StandardRs implements Serializable {
    private boolean success;
    private ErrorRs error;

    public StandardRs() {
        this.success = true;
    }

    public StandardRs(ErrorRs errorRs) {
        this.error = errorRs;
    }
}
