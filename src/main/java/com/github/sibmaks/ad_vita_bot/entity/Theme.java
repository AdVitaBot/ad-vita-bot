package com.github.sibmaks.ad_vita_bot.entity;

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
@Table(name = "theme")
public class Theme {
    @Id
    private Long id;

    @Column(name = "min_donation_amount")
    private BigDecimal minDonationAmount;

    @Column(name = "max_donation_amount")
    private BigDecimal maxDonationAmount;

    private String description;

    @OneToMany(mappedBy = "theme", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Drawing> drawings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id) &&
                Objects.equals(minDonationAmount, theme.minDonationAmount) &&
                Objects.equals(maxDonationAmount, theme.maxDonationAmount) &&
                Objects.equals(description, theme.description) &&
                Objects.equals(drawings, theme.drawings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minDonationAmount, maxDonationAmount, description, drawings);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", minDonationAmount=" + minDonationAmount +
                ", maxDonationAmount=" + maxDonationAmount +
                ", description='" + description + '\'' +
                '}';
    }
}
