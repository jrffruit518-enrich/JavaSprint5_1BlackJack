package com.example.JavaSprint5_1BlackJack.repositories;

import com.example.JavaSprint5_1BlackJack.entities.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface GameRepository extends ReactiveMongoRepository <Game, String> {
}
