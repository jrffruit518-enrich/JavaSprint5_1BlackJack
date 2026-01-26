package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import com.example.JavaSprint5_1BlackJack.entities.Player;
import com.example.JavaSprint5_1BlackJack.enums.GameResult;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.mapper.PlayerMapper;
import com.example.JavaSprint5_1BlackJack.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PlayerServiceImp implements PlayerService{

    private final PlayerRepository repository;

    @Override
    public Mono<PlayerResponse> createPlayer(PlayerRequest request) {
        Player player = new Player(request.playerName());
        return repository
                .save(player)
                .flatMap(this::enrichWithRank);
    }

    @Override
    public Mono<PlayerResponse> findPlayerById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player with ID " + id + " not found.")))
                .flatMap(this::enrichWithRank);
    }





    @Override
    public Flux<PlayerResponse> findAllPlayers() {
        return repository
                .findAllOrderByWinRateDesc()
                .index()
                .map(tuple->{
                    return PlayerMapper.toResponse(tuple.getT2(),tuple.getT1().intValue()+1);
                });
    }

    @Override
    public Mono<PlayerResponse> updatePlayerById(Long id, PlayerRequest request) {

        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player with ID " + id + " not found.")))
                .flatMap(player -> {
                    player.setPlayerName(request.playerName());
                    return repository.save(player);
                })
                .flatMap(this::enrichWithRank);
    }

    @Override
    public Mono<Void> deletePlayerById(Long id) {
       return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player with ID " + id + " not found.")))
               .flatMap(repository::delete);

    }

    @Override
    public Mono<Player> updatePlayerStats(Long playerId, GameResult result) {
        return repository.findById(playerId)
                .flatMap(player -> {
                    // Total games always increases by 1
                    player.setTotalGames(player.getTotalGames() + 1);

                    // Only increase wins if player actually won
                    if (result == GameResult.PLAYER_WON) {
                        player.setTotalWins(player.getTotalWins() + 1);
                    }

                    // Note: PUSH or DEALER_WON only affects totalGames
                    return repository.save(player);
                });
    }


    private Mono<PlayerResponse> enrichWithRank(Player player) {

        if (player.getTotalGames() == null || player.getTotalGames() == 0) {
            return Mono.just(PlayerMapper.toResponse(player, 0));
        }

        return repository.countByHigherWinRate(player.getTotalWins(), player.getTotalGames())
                .map(count -> {
                    int currentPosition = count.intValue() + 1;
                    return PlayerMapper.toResponse(player, currentPosition);
                });
    }
}
