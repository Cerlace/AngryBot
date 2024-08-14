package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class ChangeNameAction implements ActionWithCallback {

    private final UserRepository userRepository;

    @Override
    public SendMessage handle(Message message) {
        return new SendMessage(message.getChatId().toString(),
                "Хочешь новое погоняло значит? Ну говори давай, как тебя звать теперь.");
    }
    @Override
    public SendMessage callback(Message message) {
        userRepository.setUserNameByChatId(message.getText(), message.getChatId());
        return new SendMessage(message.getChatId().toString(),
                "Я запомнил, теперь буду звать тебя " + message.getText());
    }
}
