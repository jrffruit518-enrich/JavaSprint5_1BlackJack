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
@Getter
@Setter
@NoArgsConstructor
public class Game {
    @Id
    private String gameId;

    @NotNull(message = "Player information is mandatory")
    private Long playerId;

    @NotBlank(message = "Player name information is mandatory")
    private String playerName;

    private Hand dealerHand = new Hand();
    private Hand playerHand = new Hand();
    @NotNull(message = "Deck cannot be null")
    private Deck deck;
    private GameStatus gameStatus = GameStatus.PREPARING;
    private GameResult gameResult =GameResult.UNDECIDED;

    public Game(Long playerId, String playerName, Deck deck) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.deck = deck;
    }

    public void initialDeal() {
        if (deck == null) {
            throw new IllegalStateException("Deck must be initialized before dealing cards");
        }
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

    public void applyHit() {

        // Only allow HIT if it's player's turn
        if (this.gameStatus != GameStatus.PLAYER_TURN) return;
        // 1. From deck take a card and add to playerHand
        playerHand.addCard(deck.draw());

        // 2. Check if player busted (>21)
        int value = playerHand.calculateValue();
        // 3. Update gameStatus/gameResult if busted
        if (value> 21) {
            this.gameStatus = GameStatus.GAMEOVER;
            this.gameResult = GameResult.DEALER_WON;
        }

    }

    public void applyStand() {
        if (this.gameStatus != GameStatus.PLAYER_TURN) return;

        this.gameStatus = GameStatus.DEALER_TURN;

        // 1. Dealer must draw until 17 or more
        while (dealerHand.calculateValue() < 17) {
            dealerHand.addCard(deck.draw());
        }

        // 2. Decide the outcome
        determineWinner();

        // 3. Mark game as finished
        this.gameStatus = GameStatus.GAMEOVER;
    }

    private void determineWinner() {
        int pValue = playerHand.calculateValue();
        int dValue = dealerHand.calculateValue();

        if (dValue > 21) {
            // Dealer busted, player wins
            this.gameResult = GameResult.PLAYER_WON;
        } else if (pValue > dValue) {
            this.gameResult = GameResult.PLAYER_WON;
        } else if (pValue < dValue) {
            this.gameResult = GameResult.DEALER_WON;
        } else {
            this.gameResult = GameResult.PUSH; // It's a tie
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
