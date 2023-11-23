package com.github.sibmaks.ad_vita_bot.repository;

import com.github.sibmaks.ad_vita_bot.entity.UpdateTask;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UpdateTaskRepository extends CrudRepository<UpdateTask, Integer> {

    @Query(value = "select * from {h-schema}update_queue where id = ?1 FOR UPDATE", nativeQuery = true)
    UpdateTask findByIdForUpdate(Integer id);

    @Modifying
    @Query(value = "update {h-schema}update_queue set status=?2, modified_at = current_timestamp where id=?1",
            nativeQuery = true)
    void setStatus(int id, String status);

    @Modifying
    @Query(value = "update {h-schema}update_queue set status=?2, status_description=?3, modified_at = current_timestamp where id=?1",
            nativeQuery = true)
    void setStatusAndStatusDescription(int id, String status, String message);

    @Query(value = "select * from {h-schema}update_queue where status != 'PROCESSED' AND MOD(chat_id, ?1) = ?2 order by created_at for update skip locked",
            nativeQuery = true)
    UpdateTask findUnprocessedUpdateInBucket(int buckets, int bucket);
}
