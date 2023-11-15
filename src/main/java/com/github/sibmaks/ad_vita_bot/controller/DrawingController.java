package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.DeleteDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rq.UploadDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rs.UploadDrawingRs;
import com.github.sibmaks.ad_vita_bot.auth.Authorized;
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

    @Authorized
    @GetMapping(path = "get/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] get(@PathVariable(name = "id") String id) {
        var drawingId = Long.valueOf(id);
        var drawing = drawingService.getById(drawingId);
        return drawing.getImage();
    }

    @Authorized
    @PostMapping(path = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public UploadDrawingRs upload(@RequestBody @Validated UploadDrawingRq rq) {
        var drawing = drawingService.create(rq.getThemeId(), rq.getCaption(), rq.getContent());
        return new UploadDrawingRs(drawing.getId());
    }

    @Authorized
    @PostMapping(path = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody @Validated DeleteDrawingRq rq) {
        drawingService.delete(rq.getId());
    }

}
