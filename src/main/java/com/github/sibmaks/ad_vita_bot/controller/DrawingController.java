package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.DeleteDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rq.UploadDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rs.UploadDrawingRs;
import com.github.sibmaks.ad_vita_bot.service.DrawingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api/drawing/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DrawingController {
    private final DrawingService drawingService;

    @GetMapping(path = "get/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] get(@PathVariable(name = "id") String id) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        var drawingId = Long.valueOf(id);
        var drawing = drawingService.getById(drawingId);
        return drawing.getImage();
    }

    @PostMapping(path = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public UploadDrawingRs upload(@RequestBody @Validated UploadDrawingRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        var drawing = drawingService.create(rq.getThemeId(), rq.getCaption(), rq.getContent());
        return new UploadDrawingRs(drawing.getId());
    }

    @PostMapping(path = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody @Validated DeleteDrawingRq rq) {
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        drawingService.delete(rq.getId());
    }

}
