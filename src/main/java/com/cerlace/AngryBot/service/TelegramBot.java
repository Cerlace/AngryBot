package com.cerlace.AngryBot.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    private final CommandService commandService;

    public TelegramBot(@Value("${bot.name}") String botUsername,
                       @Value("${bot.token}") String botToken,
                       TelegramBotsApi telegramBotsApi,
                       SetMyCommands setMyCommands,
                       CommandService commandService) throws TelegramApiException {
        super(botToken);
        this.botUsername = botUsername;
        this.commandService = commandService;
        telegramBotsApi.registerBot(this);
        execute(setMyCommands);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() || update.getMessage().hasText()) {
            Message request = update.getMessage();
            SendMessage response = new SendMessage();
            response.setChatId(request.getChatId());

            response = commandService.processMessage(response, update.getMessage());

            try {
                execute(response);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
