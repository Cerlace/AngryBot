package com.cerlace.angrybot.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ActionWithCallback extends Action {
    void callback(SendMessage response, Message request);
}
