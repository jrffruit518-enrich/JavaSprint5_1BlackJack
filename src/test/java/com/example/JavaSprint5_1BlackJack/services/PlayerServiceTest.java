package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerRankingResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import com.example.JavaSprint5_1BlackJack.entities.Player;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    PlayerRepository repository;

    @InjectMocks
    PlayerServiceImp playerService;

    @Test
    void createPlayer_ShouldReturnResponse_WhenSuccessful() {
        // 1. Prepare Mock Data
        PlayerRequest request = new PlayerRequest("JohnDoe");

        Player savedPlayer = new Player(1L,"JohnDoe",0,0);

        // 2. Mock Repository Behavior
        // We tell Mockito: when save() is called with ANY player, return a Mono of savedPlayer
        when(repository.save(any(Player.class))).thenReturn(Mono.just(savedPlayer));

        // 3. Execute and Verify using StepVerifier
        Mono<PlayerRankingResponse> result = playerService.createPlayer(request);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    // Assertions inside the reactive stream
                    return response.playerName().equals("JohnDoe") &&
                            response.position() == 0 &&
                            response.totalWins() == 0;
                })
                .verifyComplete(); // Ensure the Mono finishes correctly
    }

    @Test
    void createPlayer_ShouldReturnError_WhenDatabaseFails() {
        // 1. Prepare Data
        PlayerRequest request = new PlayerRequest("JohnDoe");

        // 2. Mock Repository to emit an Error Signal
        // We simulate a database exception
        when(repository.save(any(Player.class)))
                .thenReturn(Mono.error(new RuntimeException("Database Connection Failed")));

        // 3. Execute and Verify using StepVerifier
        Mono<PlayerRankingResponse> result = playerService.createPlayer(request);

        StepVerifier.create(result)
                .expectErrorSatisfies(throwable -> {
                    // Verify the type and message of the error
                    assert throwable instanceof RuntimeException;
                    assert throwable.getMessage().equals("Database Connection Failed");
                })
                .verify(); // Important: for error tests, use verify() instead of verifyComplete()
    }

    @Test
    void findPlayerById_Success() {
        Player player = new Player(1L, "Alice", 10, 20);
        when(repository.findById(1L)).thenReturn(Mono.just(player));

        StepVerifier.create(playerService.findPlayerById(1L))
                .expectNextMatches(resp -> resp.playerName().equals("Alice"))
                .verifyComplete();
    }

    @Test
    void findPlayerById_Failure_NotFound() {
        when(repository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(playerService.findPlayerById(1L))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void findAllPlayers_Success() {
        Player p1 = new Player(1L, "Alice", 5, 10);
        Player p2 = new Player(2L, "Bob", 3, 10);
        when(repository.findAll()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(playerService.findAllPlayers())
                .expectNextCount(2) // 验证发出了两个元素
                .verifyComplete();
    }

    @Test
    void findAllPlayers_Failure_DatabaseError() {
        when(repository.findAll()).thenReturn(Flux.error(new RuntimeException("DB Error")));

        StepVerifier.create(playerService.findAllPlayers())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void updatePlayerById_Success() {
        Player existing = new Player(1L, "OldName", 0, 0);
        PlayerRequest request = new PlayerRequest("NewName");
        Player saved = new Player(1L, "NewName", 0, 0);

        when(repository.findById(1L)).thenReturn(Mono.just(existing));
        when(repository.save(any(Player.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(playerService.updatePlayerById(1L, request))
                .expectNextMatches(resp -> resp.playerName().equals("NewName"))
                .verifyComplete();
    }

    @Test
    void updatePlayerById_Failure_NotFound() {
        when(repository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(playerService.updatePlayerById(1L, new PlayerRequest("NewName")))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void deletePlayerById_Success() {
        // deleteById returns Mono<Void>, which translates to Mono.empty() in testing
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(playerService.deletePlayerById(1L))
                .verifyComplete(); // 验证是否发出了完成信号
    }

    @Test
    void deletePlayerById_Failure_DatabaseError() {
        when(repository.deleteById(1L)).thenReturn(Mono.error(new RuntimeException("Delete failed")));

        StepVerifier.create(playerService.deletePlayerById(1L))
                .expectError(RuntimeException.class)
                .verify();
    }


}
