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

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateTaskExecutor {

    private final UpdateTaskService updateTaskService;
    private final ObjectMapper objectMapper;
    private final BotStateService botStateService;


    @Transactional
    public boolean getAndProcess(int buckets, int bucket) {
        var updateTask = updateTaskService.findTaskAndLock(buckets, bucket);
        if(updateTask == null) {
            return false;
        }
        processUpdateTask(updateTask);
        return true;
    }

    private void processUpdateTask(UpdateTask updateTask) {
        try {
            var content = updateTask.getContent();
            var update = objectMapper.readValue(content, Update.class);
            botStateService.update(updateTask.getChatId(), update);
            updateTaskService.processed(updateTask.getId());
        } catch (Exception e) {
            log.error("Update task processing error", e);
            var message = getFailMessage(e);
            updateTaskService.fail(updateTask.getId(), message);
        }
    }

    @NotNull
    private static String getFailMessage(Exception e) {
        var message = e.getMessage();
        if(message != null) {
            if(message.length() > 512) {
                message = message.substring(0, 512);
            }
        } else {
            message = e.getClass().getName();
        }
        return message;
    }

}
