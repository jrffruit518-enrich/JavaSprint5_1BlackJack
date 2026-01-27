package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.GameRequest;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayRequest;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerResponse;
import com.example.JavaSprint5_1BlackJack.entities.Card;
import com.example.JavaSprint5_1BlackJack.entities.Deck;
import com.example.JavaSprint5_1BlackJack.entities.Game;
import com.example.JavaSprint5_1BlackJack.entities.Player;
import com.example.JavaSprint5_1BlackJack.enums.*;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.repositories.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    GameRepository gameRepository;

    @Mock
    PlayerService playerService;

    @InjectMocks
    GameServiceImp gameService;


    @Test
    void createGame_Success() {

        Long pId = 1L;
        PlayerResponse mockResponse = new PlayerResponse(1, pId, "PlayerOne", 0, 0, 0.0);

        GameRequest request = new GameRequest(pId, "PlayerOne");

        when(playerService.findPlayerById(pId)).thenReturn(Mono.just(mockResponse));

        when(gameRepository.save(any(Game.class))).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(gameService.createGame(request))
                .assertNext(response -> {
                    assertEquals("PlayerOne", response.playerName());
                })
                .verifyComplete();
    }

    @Test
    void createGame_PlayerNotFound_Failure() {
        // Given
        GameRequest request = new GameRequest(99L, "Unknown");

        // Mock player not found
        when(playerService.findPlayerById(99L)).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(gameService.createGame(request))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void playGame_Hit_Success() {
        // 1. Setup a controlled environment to avoid randomness
        // Creating cards that guarantee the player won't bust
        List<Card> fixedCards = new ArrayList<>();
        fixedCards.add(new Card( Suit.CLUBS,Rank.TWO));   // Player card 1
        fixedCards.add(new Card(Suit.DIAMONDS,Rank.TWO)); // Dealer card 1 (Upcard)
        fixedCards.add(new Card(Suit.CLUBS,Rank.THREE )); // Player card 2
        fixedCards.add(new Card(Suit.DIAMONDS,Rank.THREE)); // Dealer card 2
        fixedCards.add(new Card(Suit.HEARTS,Rank.FOUR));  // The card to be drawn on HIT

        Deck deck = new Deck(fixedCards);
        Game game = new Game(1L, "PlayerOne", deck);

        // 2. Prepare game state
        game.initialDeal();
        game.setGameStatus(GameStatus.PLAYER_TURN);

        String gameId = "game123";
        game.setGameId(gameId);

        // 3. Stubbing only the methods that ARE called
        // Since player total is 9, updatePlayerStats will NOT be triggered.
        // We remove playerService.updatePlayerStats to avoid UnnecessaryStubbingException.
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));

        // 4. Execution
        PlayRequest hitRequest = new PlayRequest(MoveType.HIT);

        StepVerifier.create(gameService.playGame(gameId, hitRequest))
                .assertNext(response -> {
                    // 5. Assertions
                    assert response.playerCards().size() == 3;
                    assert response.playerValue() == 9;
                    assert response.gameStatus() == GameStatus.PLAYER_TURN;
                })
                .verifyComplete();
    }

    @Test
    void playGame_AlreadyFinished_Failure() {
        // Given
        String gameId = "game123";
        PlayRequest request = new PlayRequest(MoveType.HIT);
        Game finishedGame = new Game();
        finishedGame.setGameStatus(GameStatus.GAMEOVER);

        when(gameRepository.findById(gameId)).thenReturn(Mono.just(finishedGame));

        // When & Then
        StepVerifier.create(gameService.playGame(gameId, request))
                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
                        throwable.getMessage().equals("Game is already finished"))
                .verify();
    }

    @Test
    void findGameById_success() {
        // given
        String gameId = "game123";
        Game game = new Game(1L, "Alice", Deck.createNewShuffledDeck());
        game.setGameId(gameId);

        Mockito.when(gameRepository.findById(gameId))
                .thenReturn(Mono.just(game));

        // when
        Mono<GameResponse> result = gameService.findGameById(gameId);

        // then
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(gameId, response.gameId());
                    assertEquals("Alice", response.playerName());
                })
                .verifyComplete();
    }

    @Test
    void findGameById_notFound() {
        // given
        String gameId = "missing-id";

        Mockito.when(gameRepository.findById(gameId))
                .thenReturn(Mono.empty());

        // when
        Mono<GameResponse> result = gameService.findGameById(gameId);

        // then
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void deleteGameById_success() {
        // given
        String gameId = "game123";
        Game game = new Game(1L, "Bob", Deck.createNewShuffledDeck());
        game.setGameId(gameId);

        Mockito.when(gameRepository.findById(gameId))
                .thenReturn(Mono.just(game));

        Mockito.when(gameRepository.delete(game))
                .thenReturn(Mono.empty());

        // when
        Mono<Void> result = gameService.deleteGameById(gameId);

        // then
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(gameRepository).delete(game);
    }


    @Test
    void deleteGameById_notFound() {
        // given
        String gameId = "missing-id";

        Mockito.when(gameRepository.findById(gameId))
                .thenReturn(Mono.empty());

        // when
        Mono<Void> result = gameService.deleteGameById(gameId);

        // then
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();

        Mockito.verify(gameRepository, Mockito.never()).delete(Mockito.any());
    }

}
