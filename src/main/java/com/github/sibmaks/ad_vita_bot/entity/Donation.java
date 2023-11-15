package com.github.sibmaks.ad_vita_bot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @Column(name = "donator_name")
    private String donatorName;

    @Column(name = "donator_email")
    private String donatorEmail;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    @JsonBackReference
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "drawing_id")
    @JsonBackReference
    private Drawing drawing;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "donation_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime donationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Donation donation = (Donation) o;

        if (!Objects.equals(amount, donation.amount)) return false;
        if (!Objects.equals(id, donation.id)) return false;
        if (status != donation.status) return false;
        if (!Objects.equals(donationDate, donation.donationDate))
            return false;
        return Objects.equals(participant, donation.participant);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (donationDate != null ? donationDate.hashCode() : 0);
        result = 31 * result + (participant != null ? participant.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "id=" + id +
                ", status=" + status +
                ", donatorName='" + donatorName + '\'' +
                ", donatorEmail='" + donatorEmail + '\'' +
                ", participant=" + participant +
                ", drawing=" + drawing +
                ", amount=" + amount +
                ", donationDate=" + donationDate +
                '}';
    }
}
