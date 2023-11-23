package com.github.sibmaks.ad_vita_bot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.ad_vita_bot.constant.ServiceError;
import com.github.sibmaks.ad_vita_bot.entity.UpdateTask;
import com.github.sibmaks.ad_vita_bot.entity.UpdateTaskStatus;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.repository.UpdateTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.ZonedDateTime;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateTaskServiceImpl implements UpdateTaskService {
    private final UpdateTaskRepository updateTaskRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void create(long chatId, Update update) {
        var updateId = update.getUpdateId();
        var updateTask = updateTaskRepository.findByIdForUpdate(updateId);

        if(updateTask == null) {
            updateTask = buildNewTask(chatId, update, 0);
            updateTaskRepository.save(updateTask);
            return;
        }

        var createdAt = updateTask.getCreatedAt();
        // update not required if less than 7 days passed
        if(!createdAt.plusDays(7).isBefore(ZonedDateTime.now())) {
            return;
        }

        var rewrite = updateTask.getRewrite() + 1;
        updateTask = buildNewTask(chatId, update, rewrite);
        updateTaskRepository.save(updateTask);
    }

    @Override
    public UpdateTask findTaskAndLock(int buckets, int bucket) {
        return updateTaskRepository.findUnprocessedUpdateInBucket(buckets, bucket);
    }

    @Override
    public void processed(int id) {
        updateTaskRepository.setStatus(id, UpdateTaskStatus.PROCESSED.name());
    }
    @Override
    public void fail(int id, String message) {
        updateTaskRepository.setStatusAndStatusDescription(id, UpdateTaskStatus.FAIL.name(), message);
    }

    private UpdateTask buildNewTask(long chatId, Update update, int rewrite) {
        String content;
        try {
            content = objectMapper.writeValueAsString(update);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Update can't be converted to JSON", e, ServiceError.UNEXPECTED_ERROR);
        }

        return UpdateTask.builder()
                .id(update.getUpdateId())
                .chatId(chatId)
                .attempt(0)
                .rewrite(rewrite)
                .content(content)
                .createdAt(ZonedDateTime.now())
                .modifiedAt(ZonedDateTime.now())
                .status(UpdateTaskStatus.RECEIVED)
                .build();
    }
}
