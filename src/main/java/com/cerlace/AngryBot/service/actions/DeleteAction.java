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
    public SendMessage handle(Message message) {
        SendMessage reply = new SendMessage(message.getChatId().toString(),
                "Точно хочешь удалиться?");
        KeyboardRow buttons = new KeyboardRow();
        buttons.add("Да, пошло оно лесом!");
        buttons.add("Не, ну его.");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(List.of(buttons));
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        reply.setReplyMarkup(keyboardMarkup);
        return reply;
    }

    @Override
    public SendMessage callback(Message message) {
        if (message.getText().equals("Да, пошло оно лесом!")) {
            userRepository.deleteById(message.getChatId());
            return new SendMessage(message.getChatId().toString(),
                    "Ну и пошел ты, щегол! Удалил тебя из списка.");
        }
        return new SendMessage(message.getChatId().toString(),
                "Вот и не придуривайся!");
    }
}
