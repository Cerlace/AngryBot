package com.cerlace.AngryBot.repository;

import com.cerlace.AngryBot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findTop10ByOrderByReplyCountDesc();
    @Transactional
    @Modifying
    @Query("update User u set u.userName = :userName where u.chatId = :chatId")
    void setUserNameByChatId(@Param(value = "userName") String userName,
                             @Param(value = "chatId") Long chatId);
}