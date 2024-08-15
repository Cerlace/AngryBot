package com.cerlace.angrybot.config;

import com.cerlace.angrybot.service.actions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.List;
import java.util.Map;

@Configuration
public class CommandsConfig {
    @Autowired
    private ApplicationContext context;

    @Bean("commandActionMap")
    Map<String, Action> commandActionMap() {
        return Map.of(
                "/start", context.getBean(StartAction.class),
                "/my_info", context.getBean(MyInfoAction.class),
                "/joke", context.getBean(JokeAction.class),
                "/rating", context.getBean(RatingAction.class),
                "/change_name", context.getBean(ChangeNameAction.class),
                "/add_joke", context.getBean(AddJokeAction.class),
                "/add_reply", context.getBean(AddReplyAction.class),
                "/delete", context.getBean(DeleteAction.class));
    }

    @Bean
    SetMyCommands setMyCommands() {
        List<BotCommand> commandList =
                List.of(
                        new BotCommand("/start", "регистрация"),
                        new BotCommand("/my_info", "инфа о тебе"),
                        new BotCommand("/joke", "получить случайный анекдот"),
                        new BotCommand("/rating", "рейтинг пользователей"),
                        new BotCommand("/change_name", "сменить имя"),
                        new BotCommand("/add_joke", "добавить анекдот"),
                        new BotCommand("/add_reply", "добавить фразу для ответа"),
                        new BotCommand("/delete", "удалить твои данные"));

        return new SetMyCommands(commandList, new BotCommandScopeDefault(), null);
    }
}
