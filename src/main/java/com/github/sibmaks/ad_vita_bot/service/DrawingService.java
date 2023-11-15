package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Drawing;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface DrawingService {
    Drawing getById(Long drawingId);

    Drawing create(Long themeId, String caption, String content);

    void delete(Long drawingId);
}
