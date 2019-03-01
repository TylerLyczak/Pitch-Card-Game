
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
    Scene gameScene;
    //
    HashMap<String, Scene> sceneMap;
    //added
    // Player default is set to 2
    int playerNum = 2;
    ArrayList<AIPlayer> AI = new ArrayList<AIPlayer>();

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
                for (int i=0; i<playerNum-1; i++) {
                    AIPlayer bot = new AIPlayer();
                    AI.add(bot);
                    AI.get(i).hand.cards = gameDealer.dealHand();
                }
            }
        });

        exitGame.setOnAction(actionEvent -> Platform.exit());

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

        Card jackOfHearts = new Card("J", 'H', "file:src/playingcards/JH.png", 10);
        Card aceOfDiamonds = new Card("A", 'D', "file:src/playingcards/AD.png", 10);
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

        // DEALER


        Player p1 = new Player();
        p1.hand.cards = gameDealer.dealHand();

        System.out.println(AI.size());
/*
        for (int i=0; i<AI.size(); i++) {
            System.out.println("Getting cards");
            AI.get(i).hand.cards = gameDealer.dealHand();
        }
        */

        HBox playerHand = new HBox (10);


        FlowPane flow = new FlowPane();

        flow.setAlignment(Pos.CENTER);
        playerHand.setAlignment(Pos.BOTTOM_CENTER);
        /*
        for (int i=0; i<6; i++) {
            playerHand.getChildren().add(p1.hand.cards.get(i).cardButton);
        }
        */
        Pitch game = new Pitch();
        playerHand = game.updateHand(playerHand, p1);
        //flow = game.updateHane2(flow, p1);

/*
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException exc) {
                throw new Error("Unexpected Interruption", exc);
            }
        });

        thread.start();

*/

        pane.setCenter(paneCenter);

        ArrayList<Card> trickList = new ArrayList<Card>();




        //added
        gamePane.setBottom(playerHand);

        //gamePane.setBottom(flow);
        Trick testTrick = new Trick();
        //gamePane.setCenter(flow);

        game.turn = 1;

        AnimationTimer gameloop = new AnimationTimer() {
            int i = 0;
            @Override
            public void handle(long now) {
                i++;
                if(i == 360) i = 0;
                v2.setRotate(i);
                //testTrick.addCard(p1.playCard());
                



                if (i < 180) {

                    v2.setScaleX((((180.0 - (double)i)/180.0)));
                }
                else {
                    int j = i - 181;
                    v2.setScaleX((double)j/180.0);
                }

                if (game.turn == 1)   {
                    /*
                    for (int i=0; i<p1.hand.cards.size(); i++)  {
                        p1.hand.cards.get(i).cardButton.setOnAction((e -> {
                            p1.playCard(trickList);
                            game.turn = 2;
                        }));
                    }
                    */
                    // FIX: player cant make turn
                    if (p1.playCard(trickList)) {
                        System.out.println("Return true");
                        game.turn = 2;
                        p1.changeBoolButtonPress();
                    }
                    //game.turn = 2;
                }
                else if (game.turn == 2)    {
                    AI.get(0).playCard(trickList);
                    if (AI.size() == 1) { game.turn = 1;}
                    else { game.turn = 3;}
                }
                else if (game.turn == 3)    {
                    AI.get(1).playCard(trickList);
                    if (AI.size() == 2) { game.turn = 1;}
                    else { game.turn = 4;}
                }
                else if (game.turn == 4)    {
                    AI.get(2).playCard(trickList);
                    game.turn = 1;
                }

                // Adds cards back into the players hand
                if (p1.hand.cards.size() == 0)  {
                    if (gameDealer.deckSize() < 6)  { gameDealer.dealerReset();}
                    p1.hand.cards = gameDealer.dealHand();
                    //gameScene = new Scene(gamePane, 800, 800);
                    //playerHand = game.updateHand(playerHand, p1);
                    HBox newBox = new HBox(10);
                    newBox = game.updateHand(newBox, p1);
                    newBox.setAlignment(Pos.BOTTOM_CENTER);
                    gamePane.setBottom(newBox);
                }

                for (int i=0; i<AI.size(); i++) {
                    if (gameDealer.deckSize() < 6) {
                        gameDealer.dealerReset();
                    }
                    if (AI.get(i).hand.cards.size() == 0) {
                        AI.get(i).hand.cards = gameDealer.dealHand();
                    }

                }

                // Change this depending on the players and the round and shit
                if (trickList.size() == playerNum && (p1.turn%3 == 0))  {
                    gamePane.setCenter(null);
                    trickList.clear();
                    p1.changeBoolButtonPress2();
                }
                else if (trickList.size() != 0) {
                    //FlowPane trickFlow = new FlowPane();
                    StackPane trickPane = new StackPane();

                    for (int i=0; i<trickList.size(); i++)  {
                        //flow.getChildren().add(trickList.get(i).cardPic);
                        trickList.get(i).cardPic.setRotate(50*i);
                        trickPane.getChildren().add(trickList.get(i).cardPic);
                    }
                    //gamePane.setCenter(flow);
                    gamePane.setCenter(trickPane);

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

        primaryStage.setScene(sceneMap.get("welcome"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
