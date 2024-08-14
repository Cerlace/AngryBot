package com.cerlace.AngryBot.service;

import com.cerlace.AngryBot.model.Reply;
import com.cerlace.AngryBot.model.User;
import com.cerlace.AngryBot.repository.ReplyRepository;
import com.cerlace.AngryBot.repository.UserRepository;
import com.cerlace.AngryBot.service.actions.Action;
import com.cerlace.AngryBot.service.actions.ActionWithCallback;

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

    public SendMessage processMessage(SendMessage response, Message request) {

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

                return action.handle(response, request);
            } else if (callbackMap.containsKey(chatId)) {

                response = callbackMap.get(chatId).callback(response, request);
                callbackMap.remove(chatId);
                return response;

            } else {
                return setRandomReply(response, request);
            }
        } else {
            if (messageText.equals("/start")) {
                return registerUser(response, request);
            } else {
                response.setText("""
                        Ты че попутал, кто ты вообще такой?

                        Зайди нормально через /start""");
                return response;
            }
        }
    }

    private SendMessage setRandomReply(SendMessage response, Message request) {
        List<Reply> replyList = replyRepository.findAll();
        int randIndex = (int) (Math.random() * replyList.size());

        User currentUser = userRepository.findById(request.getChatId()).get();
        currentUser.setReplyCount(currentUser.getReplyCount() + 1);
        userRepository.save(currentUser);

        if (replyList.isEmpty()) {
            response.setText("Не знаю что ответить даже...");
        } else {
            response.setText(replyList.get(randIndex).getReplyText());
        }
        return response;
    }

    private SendMessage registerUser(SendMessage response, Message request) {
        User user = new User();
        user.setChatId(request.getChatId());
        user.setUserName(request.getChat().getFirstName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
        response.setText("Ну здарова, " + request.getChat().getFirstName() + "!\n\n" +
                "Я тебя запомнил, щенок. Теперь напиши мне что-нибудь, а я поставлю тебя на место!");

        return response;
    }
}
