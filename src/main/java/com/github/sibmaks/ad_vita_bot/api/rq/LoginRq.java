package com.github.sibmaks.ad_vita_bot.api.rq;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class LoginRq implements Serializable {
    @NotEmpty
    @Size(min = 4, max = 128)
    private String login;
    @NotEmpty
    @Size(min = 4, max = 128)
    private String password;
}
