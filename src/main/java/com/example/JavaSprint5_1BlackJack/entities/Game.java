package com.example.JavaSprint5_1BlackJack.entities;

import com.example.JavaSprint5_1BlackJack.enums.GameResult;
import com.example.JavaSprint5_1BlackJack.enums.GameStatus;
import com.example.JavaSprint5_1BlackJack.enums.HandStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {
    @Id
    @EqualsAndHashCode.Include
    private String gameId;

    @NotNull(message = "Player information is mandatory")
    private Long playerId;

    @NotBlank(message = "Player name information is mandatory")
    private String playerName;

    @Builder.Default
    private Hand dealerHand = new Hand();

    @Builder.Default
    private Hand playerHand = new Hand();

    @NotNull(message = "Deck cannot be null")
    private Deck deck;

    @NotNull
    @Builder.Default
    private GameStatus gameStatus = GameStatus.PREPARING;

    @NotNull
    @Builder.Default
    private GameResult gameResult =GameResult.UNDECIDED;

    public void initialDeal() {
        if (gameStatus!=GameStatus.PREPARING) {
            throw new IllegalStateException("Game has already started.");
        }
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
        checkBlackJack();
    }

    private void checkBlackJack() {

        HandStatus pStatus = playerHand.updateStatus();
        HandStatus dStatus = dealerHand.updateStatus();

        // 如果有人拿到了 BlackJack
        if (pStatus == HandStatus.BLACKJACK || dStatus == HandStatus.BLACKJACK) {
            this.gameStatus = GameStatus.GAMEOVER; // 更新状态

            if (pStatus == HandStatus.BLACKJACK && dStatus == HandStatus.BLACKJACK) {
                this.gameResult = GameResult.PUSH;
            } else if (pStatus == HandStatus.BLACKJACK) {
                this.gameResult = GameResult.PLAYER_WON;
            } else {
                this.gameResult = GameResult.DEALER_WON;
            }
        } else {
            this.gameStatus = GameStatus.PLAYER_TURN;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameId, game.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameId);
    }
}
