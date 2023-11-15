package com.github.sibmaks.ad_vita_bot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * @author axothy
 * @since 0.0.1
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Drawing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "caption")
    private String caption;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    @JsonBackReference
    private Theme theme;

    @OneToMany(mappedBy="drawing", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Donation> donations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Drawing drawing = (Drawing) o;

        if (!Objects.equals(id, drawing.id)) return false;
        if (!Objects.equals(caption, drawing.caption)) return false;
        return Objects.equals(theme, drawing.theme);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Drawing{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                '}';
    }
}
