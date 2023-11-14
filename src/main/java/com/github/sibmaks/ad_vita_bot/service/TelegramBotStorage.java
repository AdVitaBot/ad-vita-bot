package com.github.sibmaks.ad_vita_bot.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class TelegramBotStorage {
    /**
     * Get invoice provider token
     *
     * @return invoice provider token
     */
    public String getInvoiceProviderToken() {
        return "1744374395:TEST:51ada44ec2bba35a2d15";
    }

    /**
     * Get available themes
     *
     * @return themes list
     */
    public List<String> getThemes() {
        return List.of("Тема \"Котики\"", "Тема \"Слоники\"");
    }

    /**
     * Min charity amount in kopecks
     * {@see https://core.telegram.org/bots/payments#supported-currencies}
     * @return min charity
     */
    public int getMinAmount() {
        return 100_00;
    }


    /**
     * Max charity amount in kopecks
     * {@see https://core.telegram.org/bots/payments#supported-currencies}
     * @return max charity
     */
    public int getMaxAmount() {
        return 900_000_00;
    }
}
