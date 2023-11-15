package com.github.sibmaks.ad_vita_bot.api.rs;

import com.github.sibmaks.ad_vita_bot.api.ThemeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetThemesRs implements Serializable {
    private List<ThemeDto> themes;
}
