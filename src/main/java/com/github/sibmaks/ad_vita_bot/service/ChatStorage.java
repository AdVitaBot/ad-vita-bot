package com.github.sibmaks.ad_vita_bot.service;

import com.github.sibmaks.ad_vita_bot.entity.UserFlowState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class ChatStorage {
    // TODO: use DB instead
    private final Map<Long, Map<String, Object>> datas = new ConcurrentHashMap<>();

    /**
     * Get current chat state
     *
     * @param chatId chat identifier
     * @return user flow state
     */
    public UserFlowState getState(long chatId) {
        var chatData = datas.computeIfAbsent(chatId, it -> new ConcurrentHashMap<>());
        return (UserFlowState) chatData.get("state");
    }

    /**
     * Set current chat state
     *
     * @param chatId chat identifier
     * @param state new chat state
     */
    public void setState(long chatId, UserFlowState state) {
        var chatData = datas.computeIfAbsent(chatId, it -> new ConcurrentHashMap<>());
        chatData.put("state", state);
    }

    /**
     * Get chat chosen theme
     *
     * @param chatId chat identifier
     * @return chat chosen theme
     */
    public String getTheme(long chatId) {
        var chatData = datas.computeIfAbsent(chatId, it -> new ConcurrentHashMap<>());
        return (String) chatData.get("theme");
    }

    /**
     * Set chat chosen theme
     *
     * @param chatId chat identifier
     * @param theme chat chosen theme
     */
    public void setTheme(long chatId, String theme) {
        var chatData = datas.computeIfAbsent(chatId, it -> new ConcurrentHashMap<>());
        chatData.put("theme", theme);
    }

    /**
     * Get chat charity amount
     *
     * @param chatId chat identifier
     * @return chat charity amount
     */
    public String getAmount(long chatId) {
        var chatData = datas.computeIfAbsent(chatId, it -> new ConcurrentHashMap<>());
        return (String) chatData.get("amount");
    }


    /**
     * Set chat charity amount
     *
     * @param chatId chat identifier
     * @param amount chat charity amount
     */
    public void setAmount(long chatId, String amount) {
        var chatData = datas.computeIfAbsent(chatId, it -> new ConcurrentHashMap<>());
        chatData.put("amount", amount);
    }

}
