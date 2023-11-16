package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.conf.AdminProperties;
import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import com.github.sibmaks.ad_vita_bot.entity.Drawing;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.repository.DrawingRepository;
import com.github.sibmaks.ad_vita_bot.repository.ThemeRepository;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.Set;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class DrawingServiceImpl implements DrawingService {
    private static final Set<MediaType> SUPPORTED_TYPES = Set.of(
      MediaType.IMAGE_PNG, MediaType.IMAGE_JPEG, MediaType.IMAGE_GIF
    );

    private final ThemeRepository themeRepository;
    private final DrawingRepository drawingRepository;
    private final Tika tika;
    private final AdminProperties.DrawingProps drawingProps;

    public DrawingServiceImpl(ThemeRepository themeRepository,
                              DrawingRepository drawingRepository,
                              Tika tika,
                              TelegramBotProperties telegramBotProperties) {
        this.themeRepository = themeRepository;
        this.drawingRepository = drawingRepository;
        this.tika = tika;
        this.drawingProps = telegramBotProperties.getAdmin().getDrawing();
    }

    @Override
    public Drawing getById(Long drawingId) {
        return drawingRepository.findById(drawingId)
                .orElseThrow(() -> new IllegalStateException("Image %s not found".formatted(drawingId)));
    }

    @Override
    public Drawing create(Long themeId, String caption, String content) {
        var decoder = Base64.getDecoder();
        var drawingContent = decoder.decode(content);
        var maxFileSize = drawingProps.getMaxFileSize();
        if(drawingContent.length >= maxFileSize) {
            throw new ServiceException("Файл слишком большой, максимум: %d КБ".formatted(maxFileSize / 1024), ServiceError.UNSUPPORTED_TYPE);
        }
        var mimeType = tika.detect(drawingContent);
        if(isNotSupported(mimeType)) {
            throw new ServiceException("Не поддерживаемый тип файла", ServiceError.UNSUPPORTED_TYPE);
        }

        var theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme %d not found".formatted(themeId)));
        var drawing = Drawing.builder()
                .caption(caption)
                .image(drawingContent)
                .active(true)
                .theme(theme)
                .build();
        return drawingRepository.save(drawing);
    }

    private boolean isNotSupported(String mimeType) {
        var springMimeType = Optional.ofNullable(mimeType)
                .map(MediaType::parseMediaType)
                .orElse(null);
        if(springMimeType == null) {
            return true;
        }
        return !SUPPORTED_TYPES.contains(springMimeType);
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
