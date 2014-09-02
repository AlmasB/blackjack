package com.almasb.blackjack;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.almasb.blackjack.Card.Rank;

/**
 * A typical player hand
 *
 * @author Almas
 * @version 1.0
 */
public class Hand {

    private ObservableList<Card> cards = FXCollections.observableArrayList();
    private SimpleIntegerProperty value = new SimpleIntegerProperty(0);

    private int aces = 0;

    public void takeCard(Card card) {
        cards.add(card);

        if (card.rank == Rank.ACE) {
            aces++;
        }

        if (value.get() + card.value > 21 && aces > 0) {
            value.set(value.get() + card.value - 10);    //then count ace as '1' not '11'
            aces--;
        }
        else {
            value.set(value.get() + card.value);
        }
    }

    public void reset() {
        cards.clear();
        value.set(0);
        aces = 0;
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public ObservableList<Card> getCards() {
        return cards;
    }
}
