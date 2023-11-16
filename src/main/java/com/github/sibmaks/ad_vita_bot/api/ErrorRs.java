package com.github.sibmaks.ad_vita_bot.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRs implements Serializable {
    private String code;
    private String message;
}
