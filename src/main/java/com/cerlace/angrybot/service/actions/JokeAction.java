package com.cerlace.angrybot.service.actions;

import com.cerlace.angrybot.model.Joke;
import com.cerlace.angrybot.repository.JokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@RequiredArgsConstructor
@Component
public class JokeAction implements Action {

    private final JokeRepository jokeRepository;

    @Override
    public void handle(SendMessage response, Message request) {
        Joke joke = jokeRepository.getRandomJoke();

        if (joke == null) {
            response.setText("Не знаю я анекдотов!");
        } else {
            response.setText(joke.getJokeText());
            response.setParseMode(ParseMode.HTML);
        }
    }
}
