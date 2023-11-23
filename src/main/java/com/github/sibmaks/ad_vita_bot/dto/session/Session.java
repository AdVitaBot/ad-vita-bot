package com.github.sibmaks.ad_vita_bot.dto.session;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.5
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {
    private boolean authorized;
    private String sessionId;
    private long userId;
}
