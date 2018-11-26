package com.almasb.blackjack;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

/**
 * Game's logic and UI
 *
 */
public class BlackjackApp extends Application {
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 700;
	
    private Deck deck = new Deck();
    private Text message = new Text();
    private int turn = 0;
    private int numPlayers;
    private Hand[] players = new Hand[10]; 

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);
    
    private Hand dealer;
    private ObservableList<Node> cards; //Observable list of children of dealerCards
    private Card cardshow;
    private HBox dealerCards = new HBox(20);
    private HBox[] playerCards = new HBox[10]; 
    
    private Parent createContent() {
    	
    	cards = dealerCards.getChildren();
        dealer = new Hand(dealerCards.getChildren());
        
    	for(int i = 0; i < numPlayers; i++) {
    		playerCards[i] = new HBox(20);
    		//players.add(new Hand(playerCards[i].getChildren());
    		players[i] = new Hand(playerCards[i].getChildren());
    	}
    	// JAVAFX STUFF********************************
    	
        Pane root = new Pane();
        root.setPrefSize(800, 600);
        root.setStyle("-fx-font: 'Garamond'; -fx-font-size: 14pt;");
        
		Region background = new Region();
        background.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        Rectangle leftBG = new Rectangle(700, WINDOW_HEIGHT-40);
        leftBG.setArcWidth(50);
        leftBG.setArcHeight(50);
        leftBG.setFill(Color.GREEN);
        Rectangle rightBG = new Rectangle(280, WINDOW_HEIGHT-40);
        rightBG.setArcWidth(50);
        rightBG.setArcHeight(50);
        rightBG.setFill(Color.ORANGE);

        // LEFT
        VBox leftVBox = new VBox(10);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: ");
        leftVBox.getChildren().add(dealerScore);
        leftVBox.getChildren().add(dealerCards);
        leftVBox.getChildren().add(message);
        Text[] playersScore = new Text[10];
        HBox[] playersBox = new HBox[10];
        
        for(int i = 0; i < numPlayers; i++) {
        	playersScore[i] = new Text("Player "+ i + ":");
        	playersBox[i] = new HBox(5, playerCards[i], playersScore[i]);
        	playersBox[i].setAlignment(Pos.CENTER);
        	leftVBox.getChildren().add(playersBox[i]);
        }
        
        
        
        // RIGHT

        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);

        final TextField bet = new TextField("BET");
        bet.setDisable(true);
        bet.setMaxWidth(50);
        
        Text txtTurn  = new Text("Turn: ");
        txtTurn.setFont(Font.font("Britannic Bold",16));
        Button btnPlay = new Button("PLAY");
        Button btnHit = new Button("HIT");
        
        Button btnStand = new Button("STAND");
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
        btnStand.disableProperty().bind(playable.not());


        dealerScore.setFont(Font.font("Britannic Bold", 16));
        //dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.valueProperty().asString()));
        
        for(int i = 0; i < numPlayers; i++) {
        	IntegerProperty score = players[i].valueProperty();
        	playersScore[i].setFont(Font.font("Britannic Bold", 16));
        	playersScore[i].textProperty().bind(new SimpleStringProperty("Player " + i + ": ").concat(score.asString()));
        	score.addListener(obv -> {
        		if(score.intValue() >= 21) btnStand.fire();
        	});
        }
     

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });
        // INIT BUTTONS
        btnPlay.setOnAction(event -> {
        	txtTurn.textProperty().setValue("Turn: Player 0");
        	dealerScore.setText("Dealer");
        	startNewGame();
        });

        btnHit.setOnAction(event -> {
        	if(turn >= numPlayers) {
        		txtTurn.textProperty().setValue("Dealer Turn");
        		endGame();
        	}
        	else { 
        		if(players[turn].valueProperty().get() < 21) {
        			players[turn].takeCard(deck.drawCard());
        		}
        	}
    
        });

        btnStand.setOnAction(event -> {
        	turn++;
        	if(turn >= numPlayers) { 
        		txtTurn.textProperty().setValue("EndGame");
        		dealerScore.setText("Dealer: " + dealer.valueProperty().getValue());
        		endGame();
        	}else{
        			txtTurn.textProperty().setValue("Player " + turn + "'s turn");
        	}
        });
        return root;
    }
    
    private void startNewGame() {
        playable.set(true);
        message.setText("");
        
        turn = 0;

        deck.refill();
        //Dealer 
        dealer.reset();
        Card cardflipped = deck.drawCardfacedown();
        dealer.takeCard(cardflipped);
        cardshow = new Card(cardflipped.suit,cardflipped.rank);
        dealer.takeCard(deck.drawCard());
        
        //Players
        for(int i = 0; i < numPlayers; i++) {
        	players[i].reset();
        	players[i].takeCard(deck.drawCard());
        	players[i].takeCard(deck.drawCard());
        }
    }

    private void endGame() {
        playable.set(false);        
        message.setFont(Font.font("Elephant", FontPosture.ITALIC, 22));
        ArrayList<String> winners = new ArrayList<String>();
        
        int playerValue;
		while (dealer.valueProperty().get() < 17)
			dealer.takeCard(deck.drawCard());
		
        int dealerValue = dealer.valueProperty().get();
        cards.set(0, cardshow);
        
        for(int i =0; i < numPlayers; i++) {
        	playerValue = players[i].valueProperty().get();
        	//System.out.println(playerValue);
        	if(playerValue <= 21) {      	
        		if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
        			winners.add("Player " + i);
        		}
        	}
        }                
        
        if(winners.isEmpty())
        	message.setText("DEALER WON");
        else
        	message.setText("Winners against Dealer: " + winners.toString());       
       
        turn = 0;
    }
    
    public void setPrimaryStage(Stage stage) {
    	stage.setScene(new Scene(createContent()));
    	stage.setWidth(WINDOW_WIDTH);
    	stage.setHeight(WINDOW_HEIGHT);
    	stage.setResizable(false);
    	stage.setTitle("BlackJack");
    	stage.show();  
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {    	
        Text welcome = new Text("Welcome to Blackjack. Please select number of players.");        
        Button onePlayer = new Button("1 Player");
        Button twoPlayer = new Button("2 Players");
        Button threePlayer = new Button("3 Players");
        
        Image image = new Image(BlackjackApp.class.getResourceAsStream("images/back.png"));
        BackgroundImage backimage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, null);
        Background background = new Background(backimage);
        
        welcome.setFont(Font.font("Britannic Bold", FontPosture.ITALIC,24));
        welcome.setFill(Color.WHITE);
        
        onePlayer.setOnAction(e -> {
        	numPlayers = 1;
            setPrimaryStage(primaryStage);       
        });
        twoPlayer.setOnAction(e -> {
        	numPlayers = 2;
        	setPrimaryStage(primaryStage);      
        });
        threePlayer.setOnAction(e -> {
        	numPlayers = 3;
        	setPrimaryStage(primaryStage);       
        });
        
        VBox introLayout = new VBox(60);
        introLayout.setStyle("-fx-font: 'Britannic Bold'; "
        		+ "-fx-font-size: 16pt; "
        		+ "-fx-font-style: italic; "
        		+ "-fx-font-weight: bold; ");
        introLayout.setBackground(background);
        introLayout.getChildren().addAll(welcome, onePlayer, twoPlayer, threePlayer);
        introLayout.setAlignment(Pos.CENTER);
        Scene introScene = new Scene(introLayout, 600, 400);
        primaryStage.setScene(introScene);
        primaryStage.setTitle("BlackJack");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
