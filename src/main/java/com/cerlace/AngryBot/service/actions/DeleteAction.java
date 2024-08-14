package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeleteAction implements ActionWithCallback {

    private final UserRepository userRepository;

    @Override
    public SendMessage handle(SendMessage response, Message request) {
        response.setText("Точно хочешь удалиться?");
        KeyboardRow buttons = new KeyboardRow();
        buttons.add("Да, пошло оно лесом!");
        buttons.add("Не, ну его.");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(buttons));
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        response.setReplyMarkup(keyboardMarkup);
        return response;
    }

    @Override
    public SendMessage callback(SendMessage response, Message request) {
        if (request.getText().equals("Да, пошло оно лесом!")) {
            userRepository.deleteById(request.getChatId());
            response.setText("Ну и пошел ты, щегол! Удалил тебя из списка.");
        } else {
            response.setText("Вот и не придуривайся!");
        }
        return response;
    }
}
