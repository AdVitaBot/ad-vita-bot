package com.github.sibmaks.ad_vita_bot.conf;

import com.github.sibmaks.ad_vita_bot.dto.UserFlowState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("app.bot")
public class TelegramBotProperties {
    private String name;
    private String token;
    private UserFlowState initialFlowState = UserFlowState.WELCOME;
}
