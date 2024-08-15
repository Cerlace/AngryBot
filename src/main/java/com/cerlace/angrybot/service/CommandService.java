package com.cerlace.angrybot.service;

import com.cerlace.angrybot.model.Reply;
import com.cerlace.angrybot.model.User;
import com.cerlace.angrybot.repository.ReplyRepository;
import com.cerlace.angrybot.repository.UserRepository;
import com.cerlace.angrybot.service.actions.Action;
import com.cerlace.angrybot.service.actions.ActionWithCallback;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommandService {
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final Map<String, Action> commandActionMap;
    private final Map<Long, ActionWithCallback> callbackMap = new ConcurrentHashMap<>();

    public CommandService(UserRepository userRepository,
                          ReplyRepository replyRepository,
                          @Qualifier("commandActionMap") Map<String, Action> commandActionMap) {
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.commandActionMap = commandActionMap;
    }

    public void processMessage(SendMessage response, Message request) {

        String messageText = request.getText();
        long chatId = request.getChatId();

        if (userRepository.findById(chatId).isPresent()) {

            if (commandActionMap.containsKey(messageText)) {

                Action action = commandActionMap.get(messageText);

                if (action instanceof ActionWithCallback) {
                    callbackMap.put(chatId, (ActionWithCallback) action);
                } else {
                    callbackMap.remove(chatId);
                }

                action.handle(response, request);
            } else if (callbackMap.containsKey(chatId)) {

                callbackMap.get(chatId).callback(response, request);
                callbackMap.remove(chatId);

            } else {
                setRandomReply(response, request);
            }
        } else {
            if (messageText.equals("/start")) {
                registerUser(response, request);
            } else {
                response.setText("""
                        Ты че попутал, кто ты вообще такой?

                        Зайди нормально через /start""");
            }
        }
    }

    private void setRandomReply(SendMessage response, Message request) {
        Reply reply = replyRepository.getRandomReply();

        if (reply == null) {
            response.setText("Не знаю что ответить даже...");
        } else {
            response.setText(reply.getReplyText());

            userRepository.incrementReplyCount(request.getChatId());
        }
    }

    private void registerUser(SendMessage response, Message request) {
        User user = new User();
        user.setChatId(request.getChatId());
        user.setUserName(request.getChat().getFirstName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
        response.setText("Ну здарова, " + request.getChat().getFirstName() + "!\n\n" +
                "Я тебя запомнил, щенок. Теперь напиши мне что-нибудь, а я поставлю тебя на место!");
    }
}
