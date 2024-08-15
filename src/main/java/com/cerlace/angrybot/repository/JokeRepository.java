package com.cerlace.angrybot.repository;

import com.cerlace.angrybot.model.Joke;
import com.cerlace.angrybot.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JokeRepository extends JpaRepository<Joke, Long> {

    @Transactional
    @Modifying
    @Query("insert into Joke(jokeText) values (:jokeText)")
    void saveNewJoke(@Param(value = "jokeText") String jokeText);

    @Query(nativeQuery = true, value = "SELECT *  FROM joke_table ORDER BY random() LIMIT 1")
    Joke getRandomJoke();
}