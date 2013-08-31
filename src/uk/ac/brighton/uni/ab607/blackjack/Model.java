package uk.ac.brighton.uni.ab607.blackjack;

import java.util.Observable;

import uk.ac.brighton.uni.ab607.blackjack.View.DialogType;

/**
 * Inner model of the game
 * 
 * @author Almas
 * @version 1.0
 */
public class Model extends Observable {
    
    private boolean running = true;
    
    private Deck deck;
    private Player player = new Player();
    private Dealer dealer = new Dealer();
    
    private int currentBet = 0;
    
    public Model() {
        Controller.createInstance(this);
    }
    
    public void newGame() {
        deck = new Deck();
        updateObservers(DialogType.ASK_BET);
    }

    public void run() {
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        
        boolean playable = true;
        while (playable) {
            
        }
    }
    
    public boolean playerPlaceBet(int bet) {
        if (player.placeBet(bet)) {
            currentBet = bet;
            run();
            return true;
        }
        return false;
    }
    
    public void updateObservers(Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
