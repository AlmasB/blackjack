package uk.ac.brighton.uni.ab607.blackjack;

import java.util.Observable;

import uk.ac.brighton.uni.ab607.blackjack.Card.Rank;

/**
 * A typical player hand
 * 
 * @author Almas
 * @version 1.0
 */
public abstract class Hand extends Observable {

    private Deck deck;
    private int value = 0,
                aces = 0;
    
    public Hand(Deck deck) {
        this.deck = deck;
    }
    
    public void takeCard() {
        Card card = deck.drawCard();
        updateObservers(card);
        
        value += card.value;
        
        if (card.rank == Rank.ACE) {
            aces++;
        }
        
        if (value > 21 && aces > 0) {
            value -= 10;    //then count ace as '1' not '11'
            aces--;
        }
    }
    
    public void reset() {
        value = 0;
        aces = 0;
    }
    
    public int getValue() {
        return value;
    }
    
    public void updateObservers(Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
