package com.example.JavaSprint5_1BlackJack.repositories;

import com.example.JavaSprint5_1BlackJack.entities.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PlayerRepository extends R2dbcRepository <Player, Long>{
}
