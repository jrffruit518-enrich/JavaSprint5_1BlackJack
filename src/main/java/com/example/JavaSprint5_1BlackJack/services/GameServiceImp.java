package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.GameRequest;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayRequest;
import com.example.JavaSprint5_1BlackJack.entities.Deck;
import com.example.JavaSprint5_1BlackJack.entities.Game;
import com.example.JavaSprint5_1BlackJack.enums.MoveType;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.mapper.GameMapper;
import com.example.JavaSprint5_1BlackJack.repositories.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GameServiceImp implements GameService{

    private final GameRepository gameRepository;


    @Override
    public Mono<GameResponse> createGame(GameRequest request) {
        Deck deck = Deck.createNewShuffledDeck();
        Game game = new Game(request.playerId(),request.playerName(),deck);
        game.initialDeal();
        return gameRepository.save(game)
                .map(GameMapper::toResponse);
    }

    @Override
    public Mono<GameResponse> findGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game with ID " + id + " not found.")))
                .map(GameMapper::toResponse);
    }

    @Override
    public Mono<GameResponse> playGame(String id, PlayRequest request) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game not found")))
                .flatMap(game -> {
                    if (request.moveType() == MoveType.HIT) {
                        game.applyHit();
                    } else {
                        game.applyStand();
                    }return gameRepository.save(game);
                })
                .map(GameMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteGameById(String id) {
        return null;
    }
}
