package uk.ac.brighton.uni.ab607.blackjack;

import uk.ac.brighton.uni.ab607.blackjack.Card.Rank;

/**
 * A typical player hand
 * 
 * Since players can't put cards back into the deck,
 * we really only care about the hand value,
 * not what cards it has
 * 
 * @author Almas
 * @version 1.0
 */
public abstract class Hand {

    private int value = 0,
                aces = 0;
    
    public void takeCard(Card card) {
        value += card.value;
        
        if (card.rank == Rank.ACE) {
            aces++;
        }
        
        if (value > 21 && aces > 0) {
            value -= 10;    //count ace as '1' not '11'
            aces--;
        }
    }
    
    public int getValue() {
        return value;
    }
}
