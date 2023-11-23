package com.github.sibmaks.ad_vita_bot.bot.command;

import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.service.ChatStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.5
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandService implements CommandHandler {
    private final Map<String, CommandHandler> COMMANDS = Map.ofEntries(
            Map.entry("/start", this::onStart)
    );

    private final ChatStorage chatStorage;
    private final TelegramBotProperties properties;


    public boolean isCommand(Update update) {
        if(!update.hasMessage()) {
            return false;
        }
        var message = update.getMessage();
        if(!message.hasText()) {
            return false;
        }
        var text = message.getText();
        return COMMANDS.containsKey(text);
    }

    @Override
    public void handle(long chatId, Update update) {
        var message = update.getMessage();
        var text = message.getText();
        var command = COMMANDS.get(text);
        command.handle(chatId, update);
    }

    private void onStart(long chatId, Update update) {
        var initialFlowState = properties.getInitialFlowState();
        chatStorage.setState(chatId, initialFlowState);
    }

}
