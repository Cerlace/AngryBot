package com.cerlace.AngryBot.service.actions;

import com.cerlace.AngryBot.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class AddReplyAction implements ActionWithCallback {

    private final ReplyRepository replyRepository;

    @Override
    public SendMessage handle(SendMessage response, Message request) {
        response.setText("Хочешь добавить новую фразу для ответа значит? Ну вводи давай.");
        return response;
    }

    @Override
    public SendMessage callback(SendMessage response, Message request) {
        replyRepository.saveNewReply(request.getText());
        response.setText("Неплохо, а ты остроумный шкет! Запишу себе, пожалуй.");
        return response;
    }
}
