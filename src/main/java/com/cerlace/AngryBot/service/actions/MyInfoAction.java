package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.model.User;
import com.cerlace.AngryBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class MyInfoAction implements Action {

    private final UserRepository userRepository;

    @Override
    public void handle(SendMessage response, Message request) {
        User user = userRepository.findById(request.getChatId()).get();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String textBlock = """
                Вот твои данные:
                ID: %d
                Погоняло: %s
                Зарегался %s
                За это время я тебе нагрубил %d раз!""";
        response.setText(textBlock.formatted(
                user.getChatId(),
                user.getUserName(),
                user.getRegisteredAt().toLocalDateTime().format(dtf),
                user.getReplyCount()));
    }
}
