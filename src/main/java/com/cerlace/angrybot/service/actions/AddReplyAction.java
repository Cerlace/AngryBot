package com.cerlace.angrybot.service.actions;

import com.cerlace.angrybot.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class AddReplyAction implements ActionWithCallback {

    private final ReplyRepository replyRepository;

    @Override
    public void handle(SendMessage response, Message request) {
        response.setText("Хочешь добавить новую фразу для ответа значит? Ну вводи давай.");
    }

    @Override
    public void callback(SendMessage response, Message request) {
        replyRepository.saveNewReply(request.getText());
        response.setText("Неплохо, а ты остроумный шкет! Запишу себе, пожалуй.");
    }
}
