package com.example.JavaSprint5_1BlackJack.repositories;

import com.example.JavaSprint5_1BlackJack.entities.Player;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends R2dbcRepository <Player, Long>{

    @Query("SELECT COUNT(*) FROM player WHERE (total_win / total_games) > (:wins / :games)")
    Mono<Long> countByHigherWinRate(int wins, int games);

    @Query("SELECT * FROM player ORDER BY (total_win / NULLIF(total_games, 0)) DESC")
    Flux<Player> findAllOrderByWinRateDesc();
}
