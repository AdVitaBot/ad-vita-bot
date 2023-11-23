package com.github.sibmaks.ad_vita_bot.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author sibmaks
 * @since 0.0.5
 */
public interface CommandHandler {

    void handle(long chatId, Update update);

}
