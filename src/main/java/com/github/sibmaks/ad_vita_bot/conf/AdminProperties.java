package com.github.sibmaks.ad_vita_bot.conf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProperties {
    private DrawingProps drawing;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrawingProps {
        private long maxFileSize;
    }
}
