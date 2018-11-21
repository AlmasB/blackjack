package com.almasb.blackjack;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Game's logic and UI
 *
 * @author Almas Baimagambetov
 */
public class BlackjackApp extends Application {

    private Deck deck = new Deck();
    private Hand dealer, player, player2;
    private Text message = new Text();
    private int turn = 0;

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);
    private HBox player2Cards = new HBox(20);

    private Parent createContent() {
        dealer = new Hand(dealerCards.getChildren());
        player = new Hand(playerCards.getChildren());
        player2 = new Hand(player2Cards.getChildren());

        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Region background = new Region();
        background.setPrefSize(800, 600);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        Rectangle leftBG = new Rectangle(550, 560);
        leftBG.setArcWidth(50);
        leftBG.setArcHeight(50);
        leftBG.setFill(Color.GREEN);
        Rectangle rightBG = new Rectangle(230, 560);
        rightBG.setArcWidth(50);
        rightBG.setArcHeight(50);
        rightBG.setFill(Color.ORANGE);

        // LEFT
        VBox leftVBox = new VBox(10);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: ");
        Text playerScore = new Text("Player: ");
        Text player2Score = new Text("Player2: ");
        
        HBox player1box = new HBox(15, playerCards, playerScore);
        player1box.setAlignment(Pos.CENTER);
        HBox player2box = new HBox(15, player2Cards,player2Score);
        player2box.setAlignment(Pos.CENTER);
        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, player1box, player2box);

        // RIGHT

        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);

        final TextField bet = new TextField("BET");
        bet.setDisable(true);
        bet.setMaxWidth(50);
        Text txtTurn  = new Text("turn: ");
        Button btnPlay = new Button("PLAY");
        Button btnHit = new Button("HIT");
        //Button btnHit2 = new Button("HIT PLAYER 2");
        
        Button btnStand = new Button("STAND");
        //Button btnStand2 = new Button("STAND P2");
        //HBox turnHBox = new HBox(10, turn);
        HBox buttonsHBox = new HBox(10, btnHit);
        buttonsHBox.setAlignment(Pos.CENTER);
        HBox standsHbox = new HBox(10, btnStand);
        standsHbox.setAlignment(Pos.CENTER);
        
        rightVBox.getChildren().addAll(txtTurn,bet, btnPlay, buttonsHBox, standsHbox);

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        btnPlay.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        //btnHit2.disableProperty().bind(playable.not());
        btnStand.disableProperty().bind(playable.not());
        //btnStand2.disableProperty().bind(playable.not());

        //visuals
        playerScore.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.valueProperty().asString()));
        player2Score.textProperty().bind(new SimpleStringProperty("Player2 : ").concat(player2.valueProperty().asString()));
        dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.valueProperty().asString()));
        

        txtTurn.textProperty().setValue("Turn: Player 1");
        
        player.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
            	if(turn >1) {
            		endGame();
            	}else {
            		turn++;    
            		txtTurn.textProperty().setValue("Turn: Player 2");
            	}
            }
        });
        player2.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
            	if(turn >1) {
            		endGame();
            	}else {
            		turn++;
            	}
            }
        });

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

        // INIT BUTTONS

        btnPlay.setOnAction(event -> {
        	txtTurn.textProperty().setValue("Turn: Player 1");
            startNewGame();
        });

        btnHit.setOnAction(event -> {
            if(turn == 0 && player.valueProperty().get() < 21) {
            	player.takeCard(deck.drawCard());            	
            }
            else if (turn == 1 && player2.valueProperty().get() < 21) {
            	player2.takeCard(deck.drawCard());
            }
            else{
            	while (dealer.valueProperty().get() < 17) {
        			dealer.takeCard(deck.drawCard());
        		}
        		endGame();
            }
        });

        btnStand.setOnAction(event -> {
        	turn++;
        	txtTurn.textProperty().setValue("Turn: Player 2");
        	if(turn > 1) {        		
        		while (dealer.valueProperty().get() < 17) {
        			dealer.takeCard(deck.drawCard());
        		}
        		endGame();
            	txtTurn.textProperty().setValue("Turn: Player 1");
        	}            
        });
        return root;
    }

    private void startNewGame() {
        playable.set(true);
        message.setText("");

        deck.refill();

        dealer.reset();
        player.reset();
        player2.reset();

        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player2.takeCard(deck.drawCard());
        player2.takeCard(deck.drawCard());
    }

    private void endGame() {
        playable.set(false);

        int dealerValue = dealer.valueProperty().get();
        int playerValue = player.valueProperty().get();
        int player2Value = player2.valueProperty().get();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;
        
        // the order of checking is important
        /*
        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winner = "DEALER";
        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winner = "PLAYER";
        }
         
        if(dealerValue == 21 || (playerValue > 21)) 
        */
        message.setText(winner + " WON");
        turn = 0;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
