package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerRankingResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import com.example.JavaSprint5_1BlackJack.entities.Player;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.mapper.PlayerMapper;
import com.example.JavaSprint5_1BlackJack.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerServiceImp implements PlayerService{

    private final PlayerRepository repository;

    @Override
    public Mono<PlayerRankingResponse> createPlayer(PlayerRequest request) {
        Player player = new Player(request.playerName());
        return repository
                .save(player)
                .map(savedPlayer->{
                    return PlayerMapper.toResponse(savedPlayer,0);
                });
    }

    @Override
    public Mono<PlayerRankingResponse> findPlayerById(Long id) {
       return repository.findById(id)
               .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player with ID " + id + " not found.")))
               .map(player -> {
                   return PlayerMapper.toResponse(player,0);
               });
    }

    @Override
    public Flux<PlayerRankingResponse> findAllPlayers() {
        return repository
                .findAll()
                .map(player ->{
                    return PlayerMapper.toResponse(player,0);
                });
    }

    @Override
    public Mono<PlayerRankingResponse> updatePlayerById(Long id, PlayerRequest request) {

        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player with ID " + id + " not found.")))
                .flatMap(player -> {
                    player.setPlayerName(request.playerName());
                    return repository.save(player);
                })
                .map(player ->{
                    return PlayerMapper.toResponse(player,0);
                });
    }

    @Override
    public Mono<Void> deletePlayerById(Long id) {
        return repository.deleteById(id);
    }
}
