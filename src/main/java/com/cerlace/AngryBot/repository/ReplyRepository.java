package com.cerlace.AngryBot.repository;

import com.cerlace.AngryBot.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Transactional
    @Modifying
    @Query("insert into Reply(replyText) values (:replyText)")
    void saveNewReply(@Param(value = "replyText") String replyText);
}