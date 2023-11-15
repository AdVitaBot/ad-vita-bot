package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.Drawing;
import com.github.sibmaks.ad_vita_bot.repository.DrawingRepository;
import com.github.sibmaks.ad_vita_bot.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DrawingServiceImpl implements DrawingService {
    private final ThemeRepository themeRepository;
    private final DrawingRepository drawingRepository;

    @Override
    public Drawing getById(Long drawingId) {
        return drawingRepository.findById(drawingId)
                .orElseThrow(() -> new IllegalStateException("Image %s not found".formatted(drawingId)));
    }

    @Override
    public Drawing create(Long themeId, String caption, String content) {
        var decoder = Base64.getDecoder();
        var theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme %d not found".formatted(themeId)));
        var drawing = Drawing.builder()
                .caption(caption)
                .image(decoder.decode(content))
                .active(true)
                .theme(theme)
                .build();
        return drawingRepository.save(drawing);
    }

    @Override
    public void delete(Long drawingId) {
        var drawing = drawingRepository.findById(drawingId)
                .orElse(null);
        if (drawing == null) {
            return;
        }
        drawing.setActive(false);
        drawingRepository.save(drawing);
    }
}
