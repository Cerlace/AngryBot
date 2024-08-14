package com.cerlace.AngryBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "userDataTable")
public class User {
    @Id
    private Long chatId;
    private String userName;
    private Timestamp registeredAt;
    private int replyCount;
}
