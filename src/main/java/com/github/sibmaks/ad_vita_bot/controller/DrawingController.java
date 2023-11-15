package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.DeleteDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rq.UploadDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rs.UploadDrawingRs;
import com.github.sibmaks.ad_vita_bot.repository.DrawingRepository;
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
public class DrawingController {
    @Autowired
    private DrawingRepository drawingRepository;

    @GetMapping(path = "get/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] get(@PathVariable(name = "id") String id) {
        // id - идентификатор картинки
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        // надо возвращать содержимое картинки с проверкой на авторизованность
        return drawingRepository.findById(Long.valueOf(id))
                .map(it -> it.getImage())
                .orElseThrow(() -> new IllegalStateException("213"));
    }

    @PostMapping(path = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public UploadDrawingRs upload(@RequestBody @Validated UploadDrawingRq rq) {
        // загрузить картинку по определённой теме
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        return new UploadDrawingRs();
    }

    @PostMapping(path = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody @Validated DeleteDrawingRq rq) {
        // удалить картинку по id
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
    }

}
