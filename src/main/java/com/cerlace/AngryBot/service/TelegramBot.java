package com.cerlace.AngryBot.service;

import com.cerlace.AngryBot.model.Reply;
import com.cerlace.AngryBot.model.User;
import com.cerlace.AngryBot.repository.ReplyRepository;
import com.cerlace.AngryBot.repository.UserRepository;
import com.cerlace.AngryBot.service.actions.Action;
import com.cerlace.AngryBot.service.actions.ActionWithCallback;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class TelegramBot extends TelegramLongPollingBot {

    private final Map<Long, ActionWithCallback> callbackMap = new ConcurrentHashMap<>();

    private final Map<String, Action> commandActionMap;

    private final ReplyRepository replyRepository;

    private final UserRepository userRepository;

    private final String botUsername;

    public TelegramBot(@Value("${bot.name}") String botUsername,
                       @Value("${bot.token}") String botToken,
                       TelegramBotsApi telegramBotsApi,
                       ReplyRepository replyRepository,
                       UserRepository userRepository,
                       SetMyCommands setMyCommands,
                       @Qualifier("commandActionMap") Map<String, Action> commandActionMap) throws TelegramApiException {
        super(botToken);
        this.botUsername = botUsername;
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.commandActionMap = commandActionMap;
        telegramBotsApi.registerBot(this);
        execute(setMyCommands);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() || update.getMessage().hasText()) {
            Message message = update.getMessage();
            String messageText = message.getText();
            long chatId = message.getChatId();

            if (userRepository.findById(chatId).isPresent()) {
                if (commandActionMap.containsKey(messageText)) {
                    Action action = commandActionMap.get(messageText);
                    send(action.handle(message));
                    if (action instanceof ActionWithCallback) {
                        callbackMap.put(chatId, (ActionWithCallback) action);
                    } else {
                        callbackMap.remove(chatId);
                    }
                } else if (callbackMap.containsKey(chatId)) {
                    send(callbackMap.get(chatId).callback(message));
                    callbackMap.remove(chatId);
                } else {
                    sendRandomReply(message);
                }
            } else {
                if (messageText.equals("/start")) {
                    registerUser(message);
                } else {
                    send(new SendMessage(message.getChatId().toString(), """
                            Ты че попутал, кто ты вообще такой?

                            Зайди нормально через /start"""));
                }
            }
        }
    }

    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRandomReply(Message message) {
        List<Reply> replyList = replyRepository.findAll();
        int randIndex = (int) (Math.random() * replyList.size());

        User currentUser = userRepository.findById(message.getChatId()).get();
        currentUser.setReplyCount(currentUser.getReplyCount() + 1);
        userRepository.save(currentUser);

        send(new SendMessage(message.getChatId().toString(), replyList.get(randIndex).getReplyText()));
    }

    private void registerUser(Message message) {
        String answer = "Ну здарова, " + message.getChat().getFirstName() + "! Сейчас запишу тебя.";
        send(new SendMessage(message.getChatId().toString(), answer));

        User user = new User();
        user.setChatId(message.getChatId());
        user.setUserName(message.getChat().getFirstName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
        send(new SendMessage(message.getChatId().toString(),
                "Я тебя запомнил, щенок. Теперь напиши мне что-нибудь, а я поставлю тебя на место!"));
    }
}
