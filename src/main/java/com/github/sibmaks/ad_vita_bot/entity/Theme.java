package com.github.sibmaks.ad_vita_bot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Theme {
    @Id
    private Long id;

    @Column(name = "min_donation_amount")
    private int minDonationAmount;

    @Column(name = "max_donation_amount")
    private int maxDonationAmount;

    private String description;

    @OneToMany(mappedBy="theme", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Drawing> drawings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Theme theme = (Theme) o;

        if (minDonationAmount != theme.minDonationAmount) return false;
        if (maxDonationAmount != theme.maxDonationAmount) return false;
        if (!Objects.equals(id, theme.id)) return false;
        return Objects.equals(description, theme.description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + minDonationAmount;
        result = 31 * result + maxDonationAmount;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
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
