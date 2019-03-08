
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends Application {

    TextField text;
    Stage myStage;
    Scene scene;
    Scene gameScene, winnerScene;
    HashMap<String, Scene> sceneMap;
    // Player default is set to 2
    int playerNum = 2;
    ArrayList<AIPlayer> AI = new ArrayList<AIPlayer>();
    PitchDealer gameDealer = new PitchDealer();
    Player p1 = new Player();
    Pitch game = new Pitch();
    AnimationTimer gameLoop;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Pitch Card Game");

        myStage = primaryStage;
        text = new TextField();
        sceneMap = new HashMap<String, Scene>();
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
        Button exitWinner = new Button("Exit to Main Menu");

        twoPlayer.setOnAction( event -> { playerNum = 2;});

        threePlayer.setOnAction( event -> { playerNum = 3;});

        fourPlayer.setOnAction( event -> { playerNum = 4;});

        startGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myStage.setScene(sceneMap.get("game"));
                game.setAmountOfPlayers(playerNum);
                for (int i=0; i<playerNum-1; i++) {
                    AIPlayer bot = new AIPlayer();
                    AI.add(bot);
                    AI.get(i).getHand().cards = gameDealer.dealHand();
                }
            }
        });

        exitGame.setOnAction(actionEvent -> Platform.exit());

        passButton.setOnAction( (event -> { p1.setBid(1);}));
        twoPoints.setOnAction( (event -> { p1.setBid(2);}));
        threePoints.setOnAction( (event -> { p1.setBid(3);}));
        fourPoints.setOnAction( (event -> { p1.setBid(4);}));
        smudgePoints.setOnAction( (event -> { p1.setBid(5);}));

        // Added suit images
        Image pic1 = new Image("file:src/pics/diamond.png");
        ImageView v1 = new ImageView(pic1);
        v1.setFitHeight(500);
        v1.setFitWidth(500);
        v1.setPreserveRatio(true);

        Image pic2 = new Image("file:src/pics/club.png");
        ImageView v2 = new ImageView(pic2);
        v2.setFitHeight(500);
        v2.setFitWidth(500);
        v2.setPreserveRatio(true);

        Image pic3 = new Image("file:src/pics/heart.png");
        ImageView v3 = new ImageView(pic3);
        v3.setFitHeight(500);
        v3.setFitWidth(500);
        v3.setPreserveRatio(true);

        Image pic4 = new Image("file:src/pics/spade.png");
        ImageView v4 = new ImageView(pic4);
        v4.setFitHeight(500);
        v4.setFitWidth(500);
        v4.setPreserveRatio(true);

        ArrayList<ImageView> imageSuits = new ArrayList<ImageView>();
        imageSuits.add(v1);
        imageSuits.add(v2);
        imageSuits.add(v3);
        imageSuits.add(v4);

        /*
        // Gameboard image
        Image table = new Image("file:src/pics/table.png");
        //ImageView tableView = new ImageView(table);
        BackgroundSize backSize = new BackgroundSize(1280, 1000, false, false, true, false);
        BackgroundImage backImg = new BackgroundImage(table , BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backSize);
        */

        // Main menu text
        Text t = new Text();
        t.setFont(new Font(50));
        t.setWrappingWidth(500);
        t.setTextAlignment(TextAlignment.CENTER);
        t.setText("Trick Card Game");

        // Panes for the main menu and game
        BorderPane pane = new BorderPane();
        BorderPane gamePane = new BorderPane();

        pane.setPadding(new Insets(100));
        gamePane.setPadding(new Insets(100));

        //Added playerSelect and gameOptions to paneCenter
        HBox playerSelect = new HBox (100, twoPlayer, threePlayer, fourPlayer);
        playerSelect.setAlignment(Pos.BOTTOM_CENTER);

        HBox gameOptions = new HBox (100, startGame, exitGame);
        gameOptions.setAlignment(Pos.BOTTOM_CENTER);

        HBox welcomeScreenText = new HBox (20, t);
        welcomeScreenText.setAlignment(Pos.TOP_CENTER);

        VBox paneCenter = new VBox(20, welcomeScreenText, v1, playerSelect, gameOptions);
        paneCenter.setAlignment(Pos.CENTER);

        // Bid selection
        HBox bidButtons = new HBox(10, passButton, twoPoints, threePoints, fourPoints, smudgePoints);
        bidButtons.setAlignment(Pos.CENTER);
        VBox bidBoxes = new VBox(10, bidButtons, submit);
        bidBoxes.setAlignment(Pos.CENTER);

        gamePane.setCenter(bidBoxes);

        // In-game Buttons
        HBox inGameButtons = new HBox(50, newGame, exitInGame);
        inGameButtons.setAlignment(Pos.CENTER);


        // Gives player cards
        p1.getHand().setCards(gameDealer.dealHand());


        HBox playerHand = new HBox (10);
        playerHand.setAlignment(Pos.BOTTOM_CENTER);

        // Updates hbox with card buttons
        game.updateHand(playerHand, p1);

        pane.setCenter(paneCenter);



        gamePane.setBottom(playerHand);
        gamePane.setTop(inGameButtons);
        //gamePane.setBackground(new Background(backImg));


        submit.setOnAction( (event -> {
            gamePane.setCenter(null);
            int highBid = p1.getBid();
            ArrayList<Integer> bidNums = new ArrayList<Integer>();
            bidNums.add(highBid);
            for (int i=0; i<AI.size(); i++) {
                AI.get(i).updateCardHandWeights();
                AI.get(i).determineBid(bidNums);
            }
            // If bids are all one, game will reset

            System.out.println("Player bid: " + p1.getBid());
            for (int i=0; i<AI.size(); i++) {
                System.out.println("AI num: " + i + " bid: " + AI.get(i).getBid());
            }

            p1.changeCardDisability(true);
            gameDealer.setGameStart(true);
            game.setRoundEnd(false);
            game.setRoundBid(true);

        }));

        newGame.setOnAction( (event -> {
            gameLoop.stop();
            p1 = new Player();
            game = new Pitch();
            AI = new ArrayList<AIPlayer>();
            gameDealer = new PitchDealer();
            gameDealer.createDealer();
            p1.getHand().setCards(gameDealer.dealHand());
            myStage.setScene(sceneMap.get("game"));
            gamePane.setCenter(bidBoxes);
            // Updates hbox with card buttons
            playerHand.getChildren().clear();
            game.updateHand(playerHand, p1);
            gameLoop.start();
        }));

        exitWinner.setOnAction( (event -> {
            gameLoop.stop();
            p1 = new Player();
            game = new Pitch();
            AI = new ArrayList<AIPlayer>();
            gameDealer = new PitchDealer();
            gameDealer.createDealer();
            p1.getHand().setCards(gameDealer.dealHand());
            myStage.setScene(sceneMap.get("welcome"));
            gamePane.setCenter(bidBoxes);
            // Updates hbox with card buttons
            playerHand.getChildren().clear();
            game.updateHand(playerHand, p1);
            gameLoop.start();
        }));


        exitInGame.setOnAction( (event -> {
            gameLoop.stop();
            p1 = new Player();
            game = new Pitch();
            AI = new ArrayList<AIPlayer>();
            gameDealer = new PitchDealer();
            gameDealer.createDealer();
            p1.getHand().setCards(gameDealer.dealHand());
            myStage.setScene(sceneMap.get("welcome"));
            gamePane.setCenter(bidBoxes);
            // Updates hbox with card buttons
            playerHand.getChildren().clear();
            game.updateHand(playerHand, p1);
            gameLoop.start();
        }));


        gameLoop = new AnimationTimer() {
            int i = 0;
            int imageIndex = 0;

            @Override
            public void handle(long now) {
                i++;
                if(i == 360) {
                    i = 0;
                    imageIndex++;
                    if (imageIndex == 4)    {imageIndex = 0;}
                    updatePaneCenter(pane, paneCenter, welcomeScreenText, playerSelect, gameOptions, imageSuits, imageIndex);
                }
                imageSuits.get(imageIndex).setRotate(i);

                if (i < 180) {
                    imageSuits.get(imageIndex).setScaleX((((180.0 - (double)i)/180.0)));
                }
                else {
                    int j = i - 181;
                    imageSuits.get(imageIndex).setScaleX((double)j/180.0);
                }

                //System.out.println("Turn: " + game.getTurn());
                // Determines the first turn after bids are made
                if (game.determineFirstTurn(p1, AI) != 5 && game.getRoundBid())   {
                    game.setTurn(game.determineFirstTurn(p1, AI));
                    game.setRoundBid(false);
                    game.setRoundStart(true);
                    game.setRoundMiddle(true);
                }

                // Sees the first cards played and makes it trump
                game.decideTrump();

                game.updatePlayerHand(p1);


                if (game.getTrickList().size() == playerNum) {
                    game.setTurn(game.playerReceiveTrick(p1, AI, gamePane));
                    game.clearSuitsPlayed();
                    game.addTrumpSuitsPlayed();
                }

                // Checks if all the cards in each player has been played
                // If it has, it will reset each players hand
                if (game.checkRoundEnd(p1, AI)) {
                    // Calculate the score of each players won tricks
                    game.calculateScore(p1, AI, gamePane);


                    gameDealer.checkDeck(game.getAmountOfPlayers());


                    p1.giveNewHand(gameDealer.dealHand());
                    HBox newBox = new HBox(10);
                    game.updateHand(newBox, p1);
                    gamePane.setBottom(newBox);


                    for (int i=0; i<AI.size(); i++) {
                        AI.get(i).giveNewHand(gameDealer.dealHand());
                    }

                    game.clearSuitsPlayed();
                }

                if (game.getRoundEnd())  {
                    gamePane.setCenter(bidBoxes);
                    game.clearSuitsPlayed();
                    //myStage.setScene(sceneMap.get(bidSelection));

                }
                else    {
                    game.gameTurn (p1, AI, gamePane);
                }

                if (game.isWinner(p1, AI))  {
                    ArrayList<Integer> winners = game.determineWinner(p1, AI);
                    winnerScene = game.makeWinnerScene(winners, sceneMap, myStage, exitWinner);
                    sceneMap.put("winnerScreen", winnerScene);
                    myStage.setScene(sceneMap.get("winnerScreen"));
                    gameLoop.stop();
                }

                game.updateScoreVbox(gamePane, p1, AI);

            }
        };
        gameLoop.start();


        scene = new Scene(pane, 1280, 1000);
        gameScene = new Scene(gamePane, 1280, 1000);

        sceneMap.put("welcome", scene);
        sceneMap.put("game", gameScene);
        sceneMap.put("winnerScreen", winnerScene);

        primaryStage.setScene(sceneMap.get("welcome"));
        primaryStage.show();
    }

    public void updatePaneCenter (BorderPane pane, VBox paneCenter, HBox welcomeScreenText, HBox playerSelect,
                                  HBox gameOptions, ArrayList<ImageView> imageSuits, int index) {
        ImageView v = imageSuits.get(index);
        paneCenter = new VBox(20, welcomeScreenText, v, playerSelect, gameOptions);
        paneCenter.setAlignment(Pos.CENTER);
        pane.setCenter(paneCenter);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
