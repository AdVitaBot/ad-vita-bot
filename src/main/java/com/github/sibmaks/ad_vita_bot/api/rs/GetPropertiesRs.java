package com.github.sibmaks.ad_vita_bot.api.rs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPropertiesRs implements Serializable {
    private String invoiceProviderToken;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deactivationDate;
}
