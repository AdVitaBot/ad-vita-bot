package com.github.sibmaks.ad_vita_bot.bot;

import com.github.sibmaks.ad_vita_bot.bot.command.CommandService;
import com.github.sibmaks.ad_vita_bot.conf.TelegramBotProperties;
import com.github.sibmaks.ad_vita_bot.exception.SendRsException;
import com.github.sibmaks.ad_vita_bot.exception.ServiceException;
import com.github.sibmaks.ad_vita_bot.service.LocalisationService;
import com.github.sibmaks.ad_vita_bot.service.TelegramBotStorage;
import com.github.sibmaks.ad_vita_bot.service.UpdateTaskService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Telegram state bot
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    private final TelegramBotStorage telegramBotStorage;
    private final ChatIdSupplier chatIdSupplier;
    private final LocalisationService localisationService;
    private final CommandService commandService;
    private final UpdateTaskService updateTaskService;

    public TelegramBotService(DefaultBotOptions defaultBotOptions,
                              TelegramBotProperties telegramBotProperties,
                              TelegramBotStorage telegramBotStorage,
                              ChatIdSupplier chatIdSupplier,
                              LocalisationService localisationService,
                              CommandService commandService,
                              UpdateTaskService updateTaskService) {
        super(defaultBotOptions, telegramBotProperties.getToken());
        this.botUsername = telegramBotProperties.getName();
        this.telegramBotStorage = telegramBotStorage;
        this.chatIdSupplier = chatIdSupplier;
        this.localisationService = localisationService;
        this.commandService = commandService;
        this.updateTaskService = updateTaskService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = chatIdSupplier.getChatId(update);
        if (chatId == null) {
            log.warn("Can't determine chatId for update {}", update.getUpdateId());
            return;
        }
        try {
            if (preCheckDate(chatId)) {
                return;
            }
            if (commandService.isCommand(update)) {
                commandService.handle(chatId, update);
            }
            updateTaskService.create(chatId, update);
        } catch (ServiceException e) {
            log.error("[%s] Technical issue happened, code: %s".formatted(chatId, e.getServiceError()), e);
            sendTechnicalIssueMessage(chatId);
        } catch (Exception e) {
            log.error("[%s] Unexpected exception happened", e);
            sendTechnicalIssueMessage(chatId);
        }
    }

    private void sendTechnicalIssueMessage(Long chatId) {
        var command = buildTechnicalErrorMessage(chatId);
        sendSilent(chatId, "technical issue message", command);
    }

    private boolean preCheckDate(Long chatId) {
        var deactivationDate = telegramBotStorage.getDeactivationDate();
        if (!LocalDate.now().isAfter(deactivationDate)) {
            return false;
        }
        var command = buildGoodByeMessage(chatId);
        sendSync(chatId, "goodbye message", command);
        return true;
    }

    private SendMessage buildGoodByeMessage(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("goodbye_message"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    private SendMessage buildTechnicalErrorMessage(Long chatId) {
        var replyKeyboardRemove = ReplyKeyboardRemove.builder()
                .removeKeyboard(Boolean.TRUE)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(localisationService.getLocalization("technical_error_text"))
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    public <T extends Serializable, M extends BotApiMethod<T>> T sendSync(long chatId,
                                                                          String type,
                                                                          M method) {
        try {
            log.debug("[{}] Send '{}'", chatId, type);
            return execute(method);
        } catch (TelegramApiException e) {
            log.error("[%d] Send '%s' error".formatted(chatId, type), e);
            throw new SendRsException("Message sending error", e);
        }
    }

    public Message sendSync(long chatId,
                            String type,
                            SendPhoto method) {
        try {
            log.debug("[{}] Send '{}'", chatId, type);
            return execute(method);
        } catch (TelegramApiException e) {
            log.error("[%d] Send '%s' error".formatted(chatId, type), e);
            throw new SendRsException("Message sending error", e);
        }
    }

    public <T extends Serializable, M extends BotApiMethod<T>> Optional<T> sendSilent(long chatId,
                                                                                      String type,
                                                                                      M method) {
        try {
            log.debug("[{}] Send '{}'", chatId, type);
            return Optional.ofNullable(execute(method));
        } catch (TelegramApiException e) {
            log.error("[%d] Send '%s' error".formatted(chatId, type), e);
        }
        return Optional.empty();
    }
}
