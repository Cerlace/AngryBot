package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.model.User;
import com.cerlace.AngryBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RatingAction implements Action {

    private final UserRepository userRepository;

    @Override
    public void handle(SendMessage response, Message request) {
        List<User> userList = userRepository.findTop10ByOrderByReplyCountDesc();
        StringBuilder sb = new StringBuilder("Ну че, вот список самых упорных терпил:\n");
        for (User user : userList) {
            sb.append(String.format("\n %s, получил уже %d грубых ответов", user.getUserName(), user.getReplyCount()));
        }
        response.setText(sb.toString());
    }
}
