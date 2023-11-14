package com.github.sibmaks.ad_vita_bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * @author axothy
 * @since 0.0.1
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @OneToMany(mappedBy="participant")
    private List<Donation> donations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        return Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "chatId=" + chatId +
                '}';
    }
}
