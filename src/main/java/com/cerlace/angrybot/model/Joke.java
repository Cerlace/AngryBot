package com.cerlace.angrybot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "jokeTable")
public class Joke {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long jokeId;
    @Column(length = 2500)
    private String jokeText;
}
