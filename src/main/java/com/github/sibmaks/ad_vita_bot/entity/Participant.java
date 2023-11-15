package com.github.sibmaks.ad_vita_bot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy="participant", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Donation> donations;

    @Enumerated(EnumType.STRING)
    private State state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (!Objects.equals(chatId, that.chatId)) return false;
        return state == that.state;
    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "chatId=" + chatId +
                ", state=" + state +
                '}';
    }
}
