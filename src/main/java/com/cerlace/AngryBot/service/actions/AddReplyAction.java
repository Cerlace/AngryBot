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
    public SendMessage handle(Message message) {
        return new SendMessage(message.getChatId().toString(),
                "Хочешь добавить новую фразу для ответа значит? Ну вводи давай.");
    }

    @Override
    public SendMessage callback(Message message) {
        replyRepository.saveNewReply(message.getText());
        return new SendMessage(message.getChatId().toString(),
                "Неплохо, а ты остроумный шкет! Запишу себе, пожалуй.");
    }
}
