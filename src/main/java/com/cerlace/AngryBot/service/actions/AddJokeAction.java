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
    public void handle(SendMessage response, Message request) {
        response.setText("Пошутить вздумал? Ну рассказывай, посмотрим что ты там придумал.");
    }

    @Override
    public void callback(SendMessage response, Message request) {
        jokeRepository.saveNewJoke(request.getText());
        response.setText("Хехе, смешно однако. Запишу в список анекдотов.");
    }
}
