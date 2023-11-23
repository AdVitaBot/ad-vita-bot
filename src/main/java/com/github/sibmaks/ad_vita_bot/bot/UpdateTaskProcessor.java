package com.github.sibmaks.ad_vita_bot.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.entity.UpdateTask;
import com.github.sibmaks.ad_vita_bot.service.UpdateTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.TimeUnit;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateTaskProcessor {
    private final UpdateTaskExecutor executor;

    public void processMessages(int buckets, int bucket) {
        var thread = Thread.currentThread();
        while (!thread.isInterrupted()) {
            try {
                if(!executor.getAndProcess(buckets, bucket)) {
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                thread.interrupt();
                log.error("[%d] Can't process update".formatted(bucket), e);
            } catch (Exception e) {
                log.error("[%d] Can't process update".formatted(bucket), e);
            }
        }
    }

}
