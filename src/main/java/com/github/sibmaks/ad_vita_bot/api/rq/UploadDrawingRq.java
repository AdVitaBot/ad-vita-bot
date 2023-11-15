package com.github.sibmaks.ad_vita_bot.api.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.value.qual.MinLen;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadDrawingRq implements Serializable {
    @NotNull
    private Long themeId;
    @NotNull
    @MinLen(1)
    private String caption;
    /**
     * Base64 image content
     */
    @NotNull
    @MinLen(1)
    private String content;
}
