package com.github.sibmaks.ad_vita_bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "localization")
public class LocalizationEntity implements Serializable {
    @Id
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    @Column(name = "message", nullable = false)
    private String message;
}
