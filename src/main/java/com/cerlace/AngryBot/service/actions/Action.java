package com.cerlace.AngryBot.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Action {
    SendMessage handle(Message message);

}
