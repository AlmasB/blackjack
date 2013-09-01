package uk.ac.brighton.uni.ab607.blackjack;

public class Controller {
    
    enum Action {
        HIT, STAND
    };

    private static Controller instance = null;
    
    private Model model;
    
    private Controller(Model model) {
        this.model = model;
    }
    
    public static Controller createInstance(Model model) {
        instance = new Controller(model);
        return instance;
    }
    
    public static Controller getInstance() {
        return instance;
    }
    
    public void startNewGame() {
        model.newGame();
    }
    
    public boolean playerPlaceBet(int bet) {
        return model.playerPlaceBet(bet);
    }
    
    public void processAction(Action action) {
        switch(action) {
            case HIT:
                model.playerHit();
                break;
                
            case STAND:
                model.playerStand();
                break;
        }
    }
}
