package uk.ac.brighton.uni.ab607.blackjack;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import uk.ac.brighton.uni.ab607.blackjack.Controller.Action;

/**
 * Game display
 * 
 * @author Almas
 * @version 1.0
 */
@SuppressWarnings("serial")
public class View extends DoubleBufferWindow implements Observer {
    
    enum DialogType {
        ASK_BET, ASK_NAME, ASK_MOVE, SHOW_WIN, SHOW_LOSE, SHOW_NO_MONEY
    };
    
    /**
     * Width and Height of the view
     */
    private static final int W = 1280,
                             H = 720;
    
    private static final String WINDOW_TITLE = "Blackjack by Almas";
    
    private ArrayList<Card> cardsOnTable = new ArrayList<Card>();

    public View() {
        super(W, H, WINDOW_TITLE);
    }

    @Override
    protected void renderPicture(Graphics2D g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, W, H);
        
        int i = 0;
        
        g.setColor(Color.white);
        for (Card card : cardsOnTable) {
            g.drawString(card.toString(), 50, 100 + 50*i);
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof DialogType) {
            Controller controller = Controller.getInstance();
            DialogType modelCall = (DialogType) arg;
            switch(modelCall) {
                case ASK_BET:
                    boolean betPlaced = false;
                    while (!betPlaced) {
                        int bet = 0;
                        while (bet <= 0) {
                            String input = (String) showInputDialog("Enter amount of money to bet", "Place your bet");
                            try {
                                bet = Integer.parseInt(input);
                            }
                            catch (NumberFormatException e) {
                                showMessage("Please enter integer value");
                                bet = -1;
                            }
                        }
                        betPlaced = Controller.getInstance().playerPlaceBet(bet);
                        if (!betPlaced) {
                            showMessage("You don't have enough money for that bet");
                        }
                    }
                    break;
                    
                case ASK_NAME:
                    
                    break;
                    
                case ASK_MOVE:
                    Action[] options = Action.values();
                    int n = -1;
                    while (!(n >= 0 && n < options.length)) {
                        n = showOptionDialog("Choose to 'HIT' or to 'STAND'", "Choose your action",
                                (Object[]) options);
                    }
                    Controller.getInstance().processAction(options[n]);
                    break;
                    
                case SHOW_WIN:
                    showMessage("You won!");
                    controller.startNewGame();
                    break;
                    
                case SHOW_LOSE:
                    showMessage("You lost!");
                    controller.startNewGame();
                    break;
                    
                case SHOW_NO_MONEY:
                    showMessage("You have no money. Game is over");
                    System.exit(0);
                    break;
            }
        }
        else {
            if (arg != null) {
                cardsOnTable = (ArrayList<Card>) arg;
            }
        }
        
        repaint();
    }

}
