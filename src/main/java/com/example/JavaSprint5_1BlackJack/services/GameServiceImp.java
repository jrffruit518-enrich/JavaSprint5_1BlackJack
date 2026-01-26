package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.GameRequest;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayRequest;
import com.example.JavaSprint5_1BlackJack.entities.Deck;
import com.example.JavaSprint5_1BlackJack.entities.Game;
import com.example.JavaSprint5_1BlackJack.enums.GameStatus;
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

    private final PlayerService playerService;


    @Override
    public Mono<GameResponse> createGame(GameRequest request) {
        // 1. Verify player exists first
        return playerService.findPlayerById(request.playerId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player not found")))
                .flatMap(player -> {
                    // 2. Initialize deck and game
                    Deck deck = Deck.createNewShuffledDeck();
                    Game game = new Game(player.playerId(), player.playerName(), deck);

                    // 3. Deal cards (might result in GAMEOVER)
                    game.initialDeal();

                    // 4. Update stats if it's an immediate BlackJack
                    return updateStatsIfFinished(game);
                })
                .flatMap(gameRepository::save)
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

                    if (game.getGameStatus() == GameStatus.GAMEOVER) {
                        return Mono.error(new IllegalStateException("Game is already finished"));
                    }

                    if (request.moveType() == MoveType.HIT) {
                        game.applyHit();
                    } else {
                        game.applyStand();
                    }

                    return updateStatsIfFinished(game);
                })
                .flatMap(gameRepository::save)
                .map(GameMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Game with ID " + id + " not found.")))
                .flatMap(gameRepository::delete);
    }

    private Mono<Game> updateStatsIfFinished(Game game) {
        if (game.getGameStatus() == GameStatus.GAMEOVER) {
            return playerService.updatePlayerStats(game.getPlayerId(), game.getGameResult())
                    .thenReturn(game);
        }
        return Mono.just(game);
    }
}
