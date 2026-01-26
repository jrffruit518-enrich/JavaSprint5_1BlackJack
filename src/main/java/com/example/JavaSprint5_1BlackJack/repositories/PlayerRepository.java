package com.example.JavaSprint5_1BlackJack.repositories;

import com.example.JavaSprint5_1BlackJack.entities.Player;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends R2dbcRepository <Player, Long>{

    @Query("""
SELECT COUNT(*)
FROM players
WHERE (total_win * 1.0 / NULLIF(total_games, 0))
    > (:wins * 1.0 / NULLIF(:games, 0))
""")
    Mono<Long> countByHigherWinRate(int wins, int games);

    @Query("""
SELECT *
FROM players
ORDER BY (total_win * 1.0 / NULLIF(total_games, 0)) DESC
""")
    Flux<Player> findAllOrderByWinRateDesc();
}
