package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.model.Joke;
import com.cerlace.AngryBot.repository.JokeRepository;
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
        List<Joke> jokeList = jokeRepository.findAll();
        int randIndex = (int) (Math.random() * jokeList.size());

        if (jokeList.isEmpty()) {
            response.setText("Не знаю я анекдотов!");
        } else {
            response.setText(jokeList.get(randIndex).getJokeText());
            response.setParseMode(ParseMode.HTML);
        }
    }
}
