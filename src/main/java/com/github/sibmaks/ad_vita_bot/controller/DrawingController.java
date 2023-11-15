package com.github.sibmaks.ad_vita_bot.controller;

import com.github.sibmaks.ad_vita_bot.api.rq.DeleteDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rq.UploadDrawingRq;
import com.github.sibmaks.ad_vita_bot.api.rs.UploadDrawingRs;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@RestController
@RequestMapping("/drawing/")
public class DrawingController {

    @GetMapping(path = "get/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] get(@PathVariable(name = "id") String id) {
        // id - идентификатор картинки
        // accept {@link CommonConst#HEADER_SESSION_ID} as auth header
        // надо возвращать содержимое картинки с проверкой на авторизованность
        return new byte[]{};
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
