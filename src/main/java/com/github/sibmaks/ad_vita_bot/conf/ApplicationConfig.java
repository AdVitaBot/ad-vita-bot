package com.github.sibmaks.ad_vita_bot.conf;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public Tika tika() {
        return new Tika();
    }

}
