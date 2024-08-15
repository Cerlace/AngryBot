package com.cerlace.angrybot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "replyTable")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;
    private String replyText;
}
