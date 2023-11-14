package com.github.sibmaks.ad_vita_bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
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
public class Drawing {
    @Id
    private Long id;

    private String caption;

    @Lob
    private Blob image;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

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
