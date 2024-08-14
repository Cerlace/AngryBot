package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.model.User;
import com.cerlace.AngryBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class StartAction implements Action {

    private final UserRepository userRepository;

    @Override
    public SendMessage handle(Message message) {
        User user = userRepository.findById(message.getChatId()).get();
        return new SendMessage(message.getChatId().toString(),
                "Я уже знаю тебя. " + user.getUserName() + ", че тебе надо?");
    }
}
