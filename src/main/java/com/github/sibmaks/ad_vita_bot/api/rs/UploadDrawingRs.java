package com.github.sibmaks.ad_vita_bot.api.rs;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
public class UploadDrawingRs extends BodyStandardRs<Long> {
    public UploadDrawingRs(Long body) {
        super(body);
    }
}
