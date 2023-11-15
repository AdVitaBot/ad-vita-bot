package com.github.sibmaks.ad_vita_bot.api.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.value.qual.MinLen;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateThemeRq implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private BigDecimal minDonationAmount;
    @NotNull
    private BigDecimal maxDonationAmount;
    @NotNull
    @MinLen(1)
    private String description;
}
