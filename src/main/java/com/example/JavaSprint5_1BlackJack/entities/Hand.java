package com.example.JavaSprint5_1BlackJack.entities;

import com.example.JavaSprint5_1BlackJack.enums.HandStatus;
import com.example.JavaSprint5_1BlackJack.enums.Rank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data // Required for MongoDB deserialization
@NoArgsConstructor
@AllArgsConstructor

public class Hand {

    private List<Card> cards = new ArrayList<>();

    private HandStatus handStatus = HandStatus.PLAYING;

    public List<Card> getHand() {
        return List.copyOf(cards);
    }

    public void addCard(Card card) {
        if (handStatus != HandStatus.PLAYING) {
            throw new IllegalStateException("Hand is not playable");
        }
        cards.add(card);
        updateStatus();
    }

    public int getValue() {
        return calculateValue();
    }

    public int calculateValue() {

        int sum = 0;
        int countAce = 0;
        for (Card card: cards) {
            sum += card.getRank().getValue();
            if (card.getRank() == Rank.ACE) {
                countAce++;
            }

        }
        while (sum > 21 && countAce > 0) {
            sum -= 10;
            countAce--;
        }
        return sum;
    }

    public void stand() {
        if (handStatus != HandStatus.PLAYING) {
            throw new IllegalStateException("Cannot stand now");
        }
        handStatus = HandStatus.STAND;
    }

    public HandStatus updateStatus() {
        int value = calculateValue();

        if (value > 21) {
            this.handStatus = HandStatus.BUST;
        } else if (value == 21 && cards.size() == 2) {
            this.handStatus = HandStatus.BLACKJACK;
        } else {
            this.handStatus = HandStatus.PLAYING;
        }
        return this.handStatus;
    }
}
