
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;


public class Main extends Application {

    TextField text;
    Button btn;
    Stage myStage;
    Scene scene, scene2;
    //added
    Scene gameScene, bidScene;
    //
    HashMap<String, Scene> sceneMap;
    //added
    // Player default is set to 2
    int playerNum = 2;
    int bid;
    ArrayList<AIPlayer> AI = new ArrayList<AIPlayer>();
    Player p1 = new Player();
    Pitch game = new Pitch();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Pitch Card Game");

        myStage = primaryStage;
        text = new TextField();
        btn = new Button();
        sceneMap = new HashMap<String, Scene>();

        PitchDealer gameDealer = new PitchDealer();
        gameDealer.createDealer();


        // Player select buttons
        Button twoPlayer = new Button("Two Players");
        Button threePlayer = new Button("Three Players");
        Button fourPlayer = new Button("Four Players");

        Button startGame = new Button ("Start Game!");
        Button exitGame = new Button ("Exit Game!");

        Button passButton = new Button ("Pass");
        Button twoPoints = new Button("2 Points");
        Button threePoints = new Button ("3 Points");
        Button fourPoints = new Button ("4 Points");
        Button smudgePoints = new Button ("Smudge");
        Button submit = new Button ("Submit");

        Button newGame = new Button("New Game");
        Button exitInGame = new Button("Exit Game");
        Button playNextHand = new Button("Play Next Hand");

        Button testButton = new Button ();






        btn.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event){
                System.out.println(text.getText());
                text.clear();
            }
        });

        twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerNum = 2;
                System.out.println("playerNum: " + playerNum);
            }
        });

        threePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerNum = 3;
                System.out.println("playerNum: " + playerNum);
            }
        });

        fourPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerNum = 4;
                System.out.println("playerNum: " + playerNum);
            }
        });

        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start game");
                myStage.setScene(sceneMap.get("game"));
                //myStage.setScene(sceneMap.get("bidScreen"));
                game.setAmountOfPlayers(playerNum);
                for (int i=0; i<playerNum-1; i++) {
                    AIPlayer bot = new AIPlayer();
                    AI.add(bot);
                    AI.get(i).hand.cards = gameDealer.dealHand();
                }
            }
        });

        exitGame.setOnAction(actionEvent -> Platform.exit());

        passButton.setOnAction( (event -> { p1.setBid(1);}));
        twoPoints.setOnAction( (event -> { p1.setBid(2);}));
        threePoints.setOnAction( (event -> { p1.setBid(3);}));
        fourPoints.setOnAction( (event -> { p1.setBid(4);}));
        smudgePoints.setOnAction( (event -> { p1.setBid(5);}));

        newGame.setOnAction( (event -> {
            // Action to reset game and go to bid screen
        }));

        exitInGame.setOnAction( (event -> { myStage.setScene(sceneMap.get("welcome"));}));


        //replace param with name of your own picture. Make sure
        //its in the src folder
        Image pic = new Image("file:src/pics/convolk.png");
        ImageView v = new ImageView(pic);
        v.setFitHeight(500);
        v.setFitWidth(500);
        v.setPreserveRatio(true);

        //added
        Image pic2 = new Image("file:src/pics/convolk.png");
        ImageView v2 = new ImageView(pic2);
        v2.setFitHeight(500);
        v2.setFitWidth(500);
        v2.setPreserveRatio(true);

        //added
        Text t = new Text();
        t.setFont(new Font(50));
        t.setWrappingWidth(500);
        t.setTextAlignment(TextAlignment.CENTER);
        t.setText("Pitch Card Game");

        //Card jackOfHearts = new Card("J", 'H', "file:src/playingcards/JH.png", 10);
        //Card aceOfDiamonds = new Card("A", 'D', "file:src/playingcards/AD.png", 10);
        Deck deck = new Deck();
        deck.addAllCards();
        deck.shuffleDeck();
        //Card c1 = deck.drawCard();

        btn.setGraphic(v);
        //added
        testButton.setGraphic(v2);

        BorderPane pane = new BorderPane();
        //added
        BorderPane gamePane = new BorderPane();

        pane.setPadding(new Insets(100));
        //added
        gamePane.setPadding(new Insets(100));

        //Added playerSelect and gameOptions to paneCenter
        HBox playerSelect = new HBox (100, twoPlayer, threePlayer, fourPlayer);
        playerSelect.setAlignment(Pos.BOTTOM_CENTER);

        HBox gameOptions = new HBox (100, startGame, exitGame);
        gameOptions.setAlignment(Pos.BOTTOM_CENTER);

        HBox welcomeScreenText = new HBox (20, t);
        welcomeScreenText.setAlignment(Pos.TOP_CENTER);

        VBox paneCenter = new VBox(20, welcomeScreenText, v2, playerSelect, gameOptions);
        paneCenter.setAlignment(Pos.CENTER);

        // Bid selection
        //BorderPane bidSelection = new BorderPane();
        //bidSelection.setPadding(new Insets(100));
        HBox bidButtons = new HBox(10, passButton, twoPoints, threePoints, fourPoints, smudgePoints);
        bidButtons.setAlignment(Pos.CENTER);
        VBox bidBoxes = new VBox(10, bidButtons, submit);
        bidBoxes.setAlignment(Pos.CENTER);

        gamePane.setCenter(bidBoxes);

        // In-game Buttons
        HBox inGameButtons = new HBox(50, newGame, playNextHand, exitInGame);
        inGameButtons.setAlignment(Pos.CENTER);


        // Gives player cards
        p1.hand.cards = gameDealer.dealHand();


        HBox playerHand = new HBox (10);


        FlowPane flow = new FlowPane();

        flow.setAlignment(Pos.CENTER);
        playerHand.setAlignment(Pos.BOTTOM_CENTER);


        // Updates hbox with card buttons
        game.updateHand(playerHand, p1);

        pane.setCenter(paneCenter);



        //added
        gamePane.setBottom(playerHand);
        gamePane.setTop(inGameButtons);

        //gamePane.setBottom(flow);
        Trick testTrick = new Trick();

        submit.setOnAction( (event -> {
            gamePane.setCenter(null);
            int highBid = p1.getBid();
            for (int i=0; i<AI.size(); i++) {
                AI.get(i).makeBid(highBid);
                if (AI.get(i).getBid() > highBid)   {
                    highBid = AI.get(i).getBid();
                }
            }

            System.out.println("Player bid: " + p1.getBid());
            for (int i=0; i<AI.size(); i++) {
                System.out.println("AI num: " + i + " bid: " + AI.get(i).getBid());
            }

            p1.changeCardDisability(true);
            gameDealer.setGameStart(true);
            game.setRoundEnd(false);
            game.setRoundBid(true);
            //game.setRoundStart(true);

        }));
        //gamePane.setCenter(flow);


        AnimationTimer gameloop = new AnimationTimer() {
            int i = 0;
            boolean gameStart = false;

            @Override
            public void handle(long now) {
                i++;
                if(i == 360) i = 0;
                v2.setRotate(i);

                if (i < 180) {

                    v2.setScaleX((((180.0 - (double)i)/180.0)));
                }
                else {
                    int j = i - 181;
                    v2.setScaleX((double)j/180.0);
                }

                //System.out.println("Turn: " + game.getTurn());
                // Determines the first turn after bids are made
                if (game.determineFirstTurn(p1, AI) != 5 && game.getRoundBid())   {
                    game.setTurn(game.determineFirstTurn(p1, AI));
                    game.setRoundBid(false);
                    game.setRoundStart(true);
                    game.roundMiddle = true;
                }

                // Sees the first cards played and makes it trump
                game.decideTrump();

                game.updatePlayerHand(p1);



                if (game.trickList.size() == playerNum) {
                    game.setTurn(game.playerReceiveTrick(p1, AI, gamePane));
                    game.clearSuitsPlayed();
                    game.addTrumpSuitsPlayed();
                }

                // Checks if all the cards in each player has been played
                // If it has, it will reset each players hand
                if (game.checkRoundEnd(p1, AI)) {
                    // Calculate the score of each players won tricks
                    game.calculateScore(p1, AI);

                    // Check if a player has 7 points
                    // If multiple players do, then display winner and button back to home screen

                    gameDealer.checkDeck(game.amountOfPlayers);


                    p1.giveNewHand(gameDealer.dealHand());
                    HBox newBox = new HBox(10);
                    game.updateHand(newBox, p1);
                    gamePane.setBottom(newBox);


                    for (int i=0; i<AI.size(); i++) {
                        AI.get(i).giveNewHand(gameDealer.dealHand());
                    }

                    game.clearSuitsPlayed();
                    // Calc the total points in each players trick deck
                    // roundStart = true
                }

                if (game.getRoundEnd())  {
                    gamePane.setCenter(bidBoxes);
                    game.clearSuitsPlayed();
                    //myStage.setScene(sceneMap.get(bidSelection));

                }
                else    {
                    game.gameTurn (p1, AI, gamePane);
                }



                // Render

            }
        };
        gameloop.start();


        scene = new Scene(pane, 800, 800);
        gameScene = new Scene(gamePane, 800, 800);

        sceneMap.put("welcome", scene);
        sceneMap.put("gamePlay", scene2);
        sceneMap.put("game", gameScene);
        //sceneMap.put ("bidScreen", bidScene);

        primaryStage.setScene(sceneMap.get("welcome"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
