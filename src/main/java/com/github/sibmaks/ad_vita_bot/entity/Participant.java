package com.github.sibmaks.ad_vita_bot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    private UserFlowState state;

    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Donation> donations;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    @JsonBackReference
    private Theme theme;

    @Column(name = "amount")
    private BigDecimal amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;
        return Objects.equals(chatId, that.chatId) && state == that.state &&
                Objects.equals(donations, that.donations) &&
                Objects.equals(theme, that.theme) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, state, donations, theme, amount);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "chatId=" + chatId +
                ", state=" + state +
                ", donations=" + donations +
                ", theme=" + theme +
                ", amount=" + amount +
                '}';
    }
}
