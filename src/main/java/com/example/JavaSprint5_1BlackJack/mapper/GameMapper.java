package com.example.JavaSprint5_1BlackJack.mapper;

import com.example.JavaSprint5_1BlackJack.DTO.CardResponse;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.entities.Card;
import com.example.JavaSprint5_1BlackJack.entities.Game;
import com.example.JavaSprint5_1BlackJack.entities.Hand;
import com.example.JavaSprint5_1BlackJack.enums.GameStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameMapper {

    public GameResponse toResponse(Game game) {
        // Map player's cards: Entity(Enum) -> DTO(String)
        List<CardResponse> playerCards = game.getPlayerHand().getCards().stream()
                .map(this::mapToCardResponse)
                .collect(Collectors.toList());

        List<CardResponse> dealerCards;
        int dealerVisibleValue;

        // Security logic for dealer cards
        if (game.getGameStatus() == GameStatus.PLAYER_TURN) {
            // During player's turn, only the first card (up-card) is visible
            Card upCard = game.getDealerHand().getCards().get(0);
            dealerCards = List.of(mapToCardResponse(upCard));
            dealerVisibleValue = upCard.getRank().getValue();
        } else {
            // Game over or other states: all dealer cards are revealed
            dealerCards = game.getDealerHand().getCards().stream()
                    .map(this::mapToCardResponse)
                    .collect(Collectors.toList());
            dealerVisibleValue = game.getDealerHand().calculateValue();
        }

        return new GameResponse(
                game.getGameId(),
                game.getPlayerName(),
                dealerCards,
                playerCards,
                dealerVisibleValue,
                game.getPlayerHand().calculateValue(),
                game.getGameStatus(),
                game.getGameResult()
        );
    }

    /**
     * Helper to map a single Card entity to CardResponse.
     */
    private CardResponse mapToCardResponse(Card card) {
        // Convert Enum name to String for the DTO
        return new CardResponse(
                card.getSuit().name(),
                card.getRank().name()
        );
    }
}

