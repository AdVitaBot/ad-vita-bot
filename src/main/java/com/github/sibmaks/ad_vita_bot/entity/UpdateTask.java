package com.github.sibmaks.ad_vita_bot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "update_queue")
public class UpdateTask implements Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @Column(name = "content", nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UpdateTaskStatus status;
    @Column(name = "status_description")
    private String statusDescription;
    @Column(name = "attempt", nullable = false)
    private int attempt;
    @Column(name = "rewrite", nullable = false)
    private int rewrite;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    public void incAttempt() {
        this.attempt++;
    }
}
