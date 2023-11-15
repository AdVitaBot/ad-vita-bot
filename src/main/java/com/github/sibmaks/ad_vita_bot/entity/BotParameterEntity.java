package com.github.sibmaks.ad_vita_bot.entity;

import jakarta.persistence.*;
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
@Table(name = "bot_parameters")
public class BotParameterEntity implements Serializable {
    @Id
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BotParameterType type;
    @Column(name = "value", nullable = false)
    private String value;
}
