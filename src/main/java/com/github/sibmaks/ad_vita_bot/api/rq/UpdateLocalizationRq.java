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
public class UpdateLocalizationRq implements Serializable {
    @NotNull
    @MinLen(1)
    private String code;
    @NotNull
    @MinLen(1)
    private String message;
}
