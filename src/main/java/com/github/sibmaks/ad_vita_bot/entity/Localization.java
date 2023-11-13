package com.github.sibmaks.ad_vita_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Localization implements Serializable {
    private String code;
    private String message;
}
