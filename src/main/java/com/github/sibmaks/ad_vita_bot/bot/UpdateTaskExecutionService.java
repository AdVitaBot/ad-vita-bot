package com.github.sibmaks.ad_vita_bot.bot;

import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Slf4j
@Service
public class UpdateTaskExecutionService {

    private final int buckets;
    private final ExecutorService executorService;
    private final UpdateTaskProcessor updateTaskProcessor;

    public UpdateTaskExecutionService(TelegramBotProperties properties,
                                      UpdateTaskProcessor updateTaskProcessor) {
        this.buckets = properties.getMaxThreads();
        this.updateTaskProcessor = updateTaskProcessor;
        this.executorService = Executors.newFixedThreadPool(buckets);
    }

    @PostConstruct
    public void init() {
        for (int bucket = 0; bucket < buckets; bucket++) {
            var finalBucket = bucket;
            executorService.submit(() -> updateTaskProcessor.processMessages(buckets, finalBucket));
        }
    }

    @PreDestroy
    public void shutDown() {
        executorService.shutdown();
    }

}
