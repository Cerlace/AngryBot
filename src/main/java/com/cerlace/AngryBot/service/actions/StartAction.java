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
    public void handle(SendMessage response, Message request) {
        User user = userRepository.findById(request.getChatId()).get();
        response.setText("Я уже знаю тебя. " + user.getUserName() + ", че тебе надо?");
    }
}
