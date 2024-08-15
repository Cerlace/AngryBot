package com.cerlace.angrybot.service.actions;

import com.cerlace.angrybot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class ChangeNameAction implements ActionWithCallback {

    private final UserRepository userRepository;

    @Override
    public void handle(SendMessage response, Message request) {
        response.setText("Хочешь новое погоняло значит? Ну говори давай, как тебя звать теперь.");
    }

    @Override
    public void callback(SendMessage response, Message request) {
        userRepository.setUserNameByChatId(request.getText(), request.getChatId());
        response.setText("Я запомнил, теперь буду звать тебя " + request.getText());
    }
}
