package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.UpdateTask;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author sibmaks
 * @since 0.0.5
 */
public interface UpdateTaskService {

    void create(long chatId, Update update);

    UpdateTask findTaskAndLock(int buckets, int bucket);

    void processed(int id);

    void fail(int id, String message);
}
