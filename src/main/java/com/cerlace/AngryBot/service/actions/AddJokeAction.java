package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.repository.JokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class AddJokeAction implements ActionWithCallback {

    private final JokeRepository jokeRepository;
    @Override
    public SendMessage handle(Message message) {
        return new SendMessage(message.getChatId().toString(),
                "Пошутить вздумал? Ну рассказывай, посмотрим что ты там придумал.");
    }

    @Override
    public SendMessage callback(Message message) {
        jokeRepository.saveNewJoke(message.getText());
        return new SendMessage(message.getChatId().toString(),
                "Хехе, смешно однако. Запишу в список анекдотов.");
    }
}
