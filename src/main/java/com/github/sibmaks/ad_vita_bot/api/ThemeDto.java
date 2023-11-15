package com.github.sibmaks.ad_vita_bot.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ThemeDto implements Serializable {
    private long id;
    private BigDecimal minDonationAmount;
    private BigDecimal maxDonationAmount;
    private String description;
}
