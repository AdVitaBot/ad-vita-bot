package com.github.sibmaks.ad_vita_bot.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePayload implements Serializable {
    private Long chatId;
    private Long donationId;
}
