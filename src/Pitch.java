import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Pitch {
    private int turn;
    private boolean roundStart;
    private boolean roundEnd;
    private boolean roundMiddle;
    private boolean roundBid;
    private char trump;
    // Middle pile in the game. Decides trump and is cleared after turns are done
    private ArrayList<Card> trickList;
    private int amountOfPlayers;
    private int trickWinner;
    ArrayList<Character> suitsPlayed;

    Pitch ()    {
        trickList = new ArrayList<Card>();
        suitsPlayed = new ArrayList<Character>();
        roundStart = false;
        roundMiddle = false;
        roundEnd = false;
        roundBid = false;
        turn = 0;
    }

    public void setAmountOfPlayers (int num) { amountOfPlayers = num;}

    public void setRoundStart (boolean start)    { roundStart = start;}

    public void setRoundMiddle (boolean middle) { roundMiddle = middle;}

    public void setRoundEnd (boolean end)   { roundEnd = end;}

    public void setRoundBid (boolean bidBool)   { roundBid = bidBool;}

    public void setTrickList (ArrayList<Card> trick)    { trickList = trick;}

    public void setTurn (int turnNum)   { turn = turnNum;}

    public boolean getRoundStart ()    { return roundStart;}

    public boolean getRoundEnd ()   { return roundEnd;}

    public boolean getRoundBid ()   { return roundBid;}

    public int getTurn ()  { return turn;}

    public int getAmountOfPlayers ()    { return amountOfPlayers;}

    public ArrayList<Character> getSuitsPlayed ()   { return suitsPlayed;}

    public ArrayList<Card> getTrickList ()  { return trickList;}


    // Updates the HBox of the players hand with the cards
    public void updateHand (HBox hand, Player p1)   {
        for (int i=0; i<6; i++) {
            hand.getChildren().add(p1.getHand().getCards().get(i).getCardButton());
        }
        hand.setAlignment(Pos.BOTTOM_CENTER);
    }

    // Updates the middle pile with the cards played by the players
    public void updateTrickList (BorderPane gamePane, ArrayList<Card> trickList)   {
        StackPane trickPane = new StackPane();
        for (int i=0; i<trickList.size(); i++)  {
            trickList.get(i).getCardPic().setRotate(50*i);
            trickPane.getChildren().add(trickList.get(i).getCardPic());
        }
        gamePane.setCenter(trickPane);
    }

    // After turns are completed, the cards in the middle are removed.
    public void removeTrickList (Player p1, BorderPane gamePane)    {
        trickList.clear();
        p1.changeBoolButtonPress();
        gamePane.setCenter(null);
    }

    public void decideTrump ()  {
        if (roundStart && trickList.size() != 0) {
            trump = trickList.get(0).getSuit();
            roundStart = false;
            roundMiddle = true;
        }
    }

    // Checks if all bids are 1
    public boolean checkBids (Player p1, ArrayList<AIPlayer> AI) {
        if (p1.getBid() == 1)   {
            for (int i=0; i<AI.size(); i++) {
                if (AI.get(i).getBid() != 1)    {
                    return false;
                }
            }
            return true;
        }
        else    {
            return false;
        }
    }

    // Determines the highest played card of the current trick with trump played or not
    public int decideTrick ()  {
        if (amountOfPlayers != trickList.size())    { return 0;}

        ArrayList<Card> trumpCards = new ArrayList<Card>();
        for (int i=0; i<trickList.size(); i++)  {
            if (trickList.get(i).getSuit() == trump)    {
                trumpCards.add(trickList.get(i));
            }
        }

        if (trumpCards.size() == 1) { return trumpCards.get(0).getPlayedBy();}
        else if (trumpCards.size() > 1)  {
            Card highCard = trumpCards.get(0);
            for (int i=0; i<trumpCards.size(); i++) {
                if (trumpCards.get(i).getRankVal() > highCard.getRankVal())   {
                    highCard = trumpCards.get(i);
                }
            }
            return highCard.getPlayedBy();
        }
        // No trumps were played in current trick
        else {
            Card highCard = trickList.get(0);
            for (int i = 0; i < trickList.size(); i++) {
                if (trickList.get(i).getRankVal() > highCard.getRankVal()) {
                    highCard = trickList.get(i);
                }
            }
            return highCard.getPlayedBy();
        }
    }

    // Gives the winner of that turn the trick
    public int playerReceiveTrick (Player p1, ArrayList<AIPlayer> AI, BorderPane gamePane)   {
        int winner = decideTrick();

        // Human player case
        if (winner == 1)    {
            for (int i=0; i<trickList.size(); i++)  {
                p1.getTrickDeck().add(trickList.get(i));
            }
        }
        // Bot case
        else {
            for (int i=0; i<trickList.size(); i++)  {
                 AI.get(winner-2).getTrickDeck().add(trickList.get(i));
            }
        }

        removeTrickList(p1, gamePane);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        trickWinner = winner;
        return winner;
    }

    public boolean checkRoundEnd (Player p1, ArrayList<AIPlayer> AI)   {
        boolean check = false;
        if (p1.getHand().getCards().size() == 0)    {
            check = true;
        }
        else    { return false;}

        for (int i=0; i<amountOfPlayers-1; i++) {
            if (AI.get(i).getHand().getCards().size() == 0)    {
                check = true;
            }
            else    {
                check = false;
                return false;
            }
        }

        if (check)  {
            roundMiddle = false;
            roundEnd = true;
            return true;
        }
        else    { return false;}
    }

    // Calculates the final score of each players trick with there bids
    public void calculateScore (Player p1, ArrayList<AIPlayer> AI, BorderPane gamePane)  {
        // Calculate game point
        // For player
        ArrayList<Integer> gamePointList = new ArrayList<Integer>();
        if (p1.getTrickDeck().size() > 0)   {
            int amount = 0;
            for (int i=0; i<p1.getTrickDeck().size(); i++)  {
                amount += p1.getTrickDeck().get(i).getPoints();
            }
            gamePointList.add(amount);
        }
        else    {
            gamePointList.add(0);
        }

        // For bot
        for (int i=0; i<AI.size(); i++) {
            int botAmount = 0;
            for (int j=0; j<AI.get(i).getTrickDeck().size(); j++)   {
                botAmount += AI.get(i).getTrickDeck().get(j).getPoints();
            }
            gamePointList.add(botAmount);
        }

        // Calculates who gets the game point
        // This test if two players got the game point
        int highPoint = gamePointList.get(0);
        int highIndex = 0;
        boolean twoWinners = false;
        for (int i=0; i<gamePointList.size(); i++)  {
            if (gamePointList.get(i) > highPoint)   {
                highPoint = gamePointList.get(i);
                highIndex = i;
            }
        }
        for (int i=0; i<gamePointList.size(); i++)  {
            if (highIndex == i) {
                continue;
            }
            else if (highPoint == gamePointList.get(i)) {
                twoWinners = true;
                break;
            }
        }
        int gamePointPlayer = 5;
        if (!twoWinners) {
            gamePointPlayer = highIndex;
        }


        // High / Low / Jack of Trumps
        Card high = null;
        Card low = null;
        // Sets to inital value of 5 to make sure it doesn't give any player the point if not found, like jack of trump
        int playerHigh = 5;
        int playerLow = 5;
        int playerJack = 5;
        // For player
        // Checks size of the trick deck
        if (p1.getTrickDeck().size() > 0)   {
            for (int i=0; i<p1.getTrickDeck().size(); i++)  {
                // Finds which cards are trump
                if (p1.getTrickDeck().get(i).getSuit() == trump)    {
                    // If the first trump card is found, it will be the high
                    if (high == null)   {
                        high = p1.getTrickDeck().get(i);
                        playerHigh = 0;
                    }
                    // Compares any trump card to the high trump card, returns true if high is the lower value
                    else if (Card.cardSuitDecider (high.getRank(), p1.getTrickDeck().get(i).getRank()))  {
                        high = p1.getTrickDeck().get(i);
                    }

                    // If the first trump card is found, it will be the low
                    if (low == null)    {
                        low = p1.getTrickDeck().get(i);
                        playerLow = 0;
                    }
                    // Compares any trump card to the low trump card, returns true if low is the higher value
                    else if (Card.cardSuitDecider(p1.getTrickDeck().get(i).getRank(), low.getRank()))   {
                        low = p1.getTrickDeck().get(i);
                        playerLow = 0;
                    }

                    // If the jack is found, then it will set it to the player with it
                    if (p1.getTrickDeck().get(i).getRank().equals("J")) {
                        playerJack = 0;
                    }
                }
            }
        }

        // For bots
        for (int i=0; i<AI.size(); i++) {
            // Checks size of the trick deck
            if (AI.get(i).getTrickDeck().size() > 0)    {
                for (int j=0; j<AI.get(i).getTrickDeck().size(); j++)   {
                    // Finds which cards are trump
                    if (AI.get(i).getTrickDeck().get(j).getSuit() == trump) {
                        // If the first trump card is found, it will be the high
                        if (high == null)   {
                            high = AI.get(i).getTrickDeck().get(j);
                            playerHigh = i+1;
                        }
                        // Compares any trump card to the high trump card, returns true if high is the lower value
                        else if (Card.cardSuitDecider(high.getRank(), AI.get(i).getTrickDeck().get(j).getRank()))    {
                            high = AI.get(i).getTrickDeck().get(j);
                            playerHigh = i+1;
                        }

                        // If the first trump card is found, it will be the low
                        if (low == null)    {
                            low = AI.get(i).getTrickDeck().get(i);
                            playerLow = i+1;
                        }
                        // Compares any trump card to the low trump card, returns true if low is the higher value
                        else if (Card.cardSuitDecider(AI.get(i).getTrickDeck().get(i).getRank(), low.getRank()))   {
                            low = AI.get(i).getTrickDeck().get(i);
                            playerLow = i+1;
                        }

                        // If the jack is found, then it will set it to the player with it
                        if (AI.get(i).getTrickDeck().get(i).getRank().equals("J")) {
                            playerJack = i+1;
                        }
                    }
                }
            }
        }

        // Gives the point to each player, incrementing there temp points
        if (gamePointPlayer == 5)   { /* No-one gets the point*/}
        else if (gamePointPlayer == 0)   {
            p1.incrementTempPoints(1);
        }
        else    {
            AI.get(gamePointPlayer-1).incrementTempPoints(1);
        }

        if (playerHigh == 0) {
            p1.incrementTempPoints(1);
        }
        else    { AI.get(playerHigh-1).incrementTempPoints(1);}

        if (playerLow == 0) {
            p1.incrementTempPoints(1);
        }
        else    { AI.get(playerLow-1).incrementTempPoints(1);}

        if (playerJack == 5)    { /* No-one gets the point*/}
        else if (playerJack == 0)   {
            p1.incrementTempPoints(1);
        }
        else if (playerJack > 0 && playerJack < 5)  {
            AI.get(playerJack-1).incrementTempPoints(1);
        }

        // Calculates if the players got greater than or equal to their bid
        // If they bidded one and got one point, players will receive that point
        if (p1.getTempPoints() < p1.getBid() && p1.getBid() != 1)   {
            p1.decrementPoints(p1.getBid());
        }
        else if (p1.getBid() == p1.getTempPoints()) {
            p1.incrementPoints(1);
        }
        else    {
            p1.incrementPoints(p1.getBid());
        }
        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getTempPoints() < AI.get(i).getBid() && AI.get(i).getBid() != 1) {
                AI.get(i).decrementPoints(AI.get(i).getBid());
            }
            else if (AI.get(i).getBid() == AI.get(i).getTempPoints()) {
                AI.get(i).incrementPoints(1);
            }
            else    {
                AI.get(i).incrementPoints(AI.get(i).getTempPoints());
            }
        }
        // Resets all the temp points used to calculate there score
        p1.resetTempPoints();
        for (int i=0; i<AI.size(); i++) {
            AI.get(i).resetTempPoints();
        }

        displayWinners(gamePane, gamePointPlayer, playerHigh, playerLow, playerJack);

    }

    // Displayed the current winners of the round
    public void displayWinners (BorderPane gamePane, int gamePointWinner, int playerHigh, int playerLow, int playerJack) {
        // For displaying the score
        Text points = new Text();
        points.setFont(new Font(20));
        //points.setWrappingWidth(500);
        points.setTextAlignment(TextAlignment.CENTER);
        points.setText("Winners");

        // Sets the text of the winner of the game point
        Text gamePoint = new Text();
        gamePoint.setFont(new Font(10));
        gamePoint.setTextAlignment(TextAlignment.CENTER);
        if (gamePointWinner != 5)   {
            gamePoint.setText("Game Point: Player " + (gamePointWinner+1));
        }
        else    {
            gamePoint.setText("No Game Point Winner");
        }

        // Sets the text for the winner of the high of Trumps
        Text highPlayer = new Text();
        highPlayer.setFont(new Font(10));
        highPlayer.setTextAlignment(TextAlignment.CENTER);
        if (playerHigh != 5)    {
            highPlayer.setText("High of Trumps: Player " + (playerHigh+1));
        }
        else    {
            highPlayer.setText("No High of Trumps Winner");
        }

        // Sets the text for the winner of the low of trumps
        Text lowPlayer = new Text();
        lowPlayer.setFont(new Font(10));
        lowPlayer.setTextAlignment(TextAlignment.CENTER);
        if (playerLow != 5) {
            lowPlayer.setText("Low of Trumps: Player " + (playerLow+1));
        }
        else    {
            lowPlayer.setText("No Low of Trumps Winner");
        }

        // Sets the text for the winner of the jack of trumps
        Text jackPlayer = new Text();
        jackPlayer.setFont(new Font(10));
        jackPlayer.setTextAlignment(TextAlignment.CENTER);
        if (playerJack != 5)    {
            jackPlayer.setText("Jack of Trumps: Player " + (playerJack+1));
        }
        else    {
            jackPlayer.setText("No Jack of Trumps Winner");
        }

        // Makes a VBox with all the elements
        VBox winnerVbox = new VBox(10, points, gamePoint, highPlayer, lowPlayer, jackPlayer);
        winnerVbox.setAlignment(Pos.CENTER_RIGHT);
        gamePane.setRight(winnerVbox);
    }

    // Determines who goes first depending on their bid
    public int determineFirstTurn (Player p1, ArrayList<AIPlayer> AI)   {
        int highPlayer = 1;
        int highestBid = 0;
        highestBid = p1.getBid();

        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getBid() > highestBid)    {
                highestBid = AI.get(i).getBid();
                highPlayer = i+2;
            }
        }
        return highPlayer;
    }

    // Keeps track of whos turn it is and allows them to make that turn
    public boolean gameTurn (Player p1, ArrayList<AIPlayer> AI, BorderPane gamePane)    {
        if (!roundMiddle)   { return false;}
        if (turn == 1)  {
            if (p1.playCard(trickList)) {
                updateTrickList(gamePane, trickList);
                updateSuitsPlayed();
                turn = 2;
                return true;
            }
            else    { return false;}
        }
        else if (turn == 2) {
            AI.get(0).playCard(trickList, suitsPlayed,2, trump);
            updateTrickList(gamePane, trickList);
            updateSuitsPlayed();
            if (AI.size() == 1) {
                turn = 1;
                return true;
            }
            else {
                turn = 3;
                return true;
            }
        }
        else if (turn == 3) {
            AI.get(1).playCard(trickList, suitsPlayed,3, trump);
            updateTrickList(gamePane, trickList);
            updateSuitsPlayed();
            if (AI.size() == 2) { turn = 1; return true;}
            else { turn = 4; return true;}
        }
        else if (turn == 4) {
            AI.get(2).playCard(trickList, suitsPlayed,4, trump);
            updateTrickList(gamePane, trickList);
            updateSuitsPlayed();
            turn = 1;
            return true;
        }
        return false;
    }

    // Adds any suit that is played into the list
    public void updateSuitsPlayed ()    {
        for (int i=0; i<trickList.size(); i++)    {
            if (!suitsPlayed.contains(trickList.get(i).getSuit()))  {
                suitsPlayed.add(trickList.get(i).getSuit());
            }
        }
    }

    // Clears the suit array list
    public void clearSuitsPlayed () { suitsPlayed.clear();}

    public void addTrumpSuitsPlayed ()  {suitsPlayed.add(trump);}

    // Changes the players hand to the suits that were played
    public void updatePlayerHand (Player p1) {
        boolean changed = false;
        p1.changeCardDisability(true);
        for (int i=0; i<p1.getHand().getCards().size(); i++)    {
            for (int j=0; j<suitsPlayed.size(); j++)    {
                if (p1.getHand().getCards().get(i).getSuit() == suitsPlayed.get(j)) {
                    p1.getHand().getCards().get(i).getCardButton().setDisable(false);
                    changed = true;
                }
            }
        }
        // If the player has no cards of the suits played, the player can play any card
        if (!changed)   { p1.changeCardDisability(false);}

        if (trickWinner == 1 && trickList.size() == 0)    { p1.changeCardDisability(false);}
    }

    // Checks if any player reached 7 or above points
    public boolean isWinner (Player p1, ArrayList<AIPlayer> AI) {
        if (p1.getPoints() >= 7)    {
            return true;
        }
        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getPoints() >= 7) {
                return true;
            }
        }
        return false;
    }

    // Adds all the players above 7 points to an array list
    public ArrayList<Integer> determineWinner (Player p1, ArrayList<AIPlayer> AI)  {
        ArrayList<Integer> winners = new ArrayList<Integer>();
        if (p1.getPoints() >= 7)    {
            winners.add(1);
        }
        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getPoints() >= 7) {
                winners.add(i+2);
            }
        }
        return winners;
    }

    // Makes the winner scene depending who got the 7 points
    public Scene makeWinnerScene (ArrayList<Integer> winners, HashMap<String, Scene> sceneMap, Stage myStage, Button exitWinner)   {
        Text t = new Text();
        t.setFont(new Font(50));
        t.setWrappingWidth(500);
        t.setTextAlignment(TextAlignment.CENTER);

        String winnerText = "Congratulations to Player " + winners.get(0);
        for (int i=1; i<winners.size(); i++)    {
            winnerText += " And Player " + winners.get(i);
        }
        winnerText += "!";
        t.setText(winnerText);

        HBox winnerHbox = new HBox(10, t);
        winnerHbox.setAlignment(Pos.CENTER);
        VBox winnerFinal = new VBox(20, winnerHbox, exitWinner);
        winnerFinal.setAlignment(Pos.CENTER);

        Scene winnerScene = new Scene(winnerFinal, 1280, 1000);

        return winnerScene;
    }

    // Updates the score after a round if completed
    public void updateScoreVbox (BorderPane gamePane, Player p1, ArrayList<AIPlayer> AI)    {
        // For displaying the score
        Text totalPoints = new Text();
        totalPoints.setFont(new Font(20));
        totalPoints.setTextAlignment(TextAlignment.LEFT);
        totalPoints.setText("Total Points");

        // Adds the player
        Text p1Points = new Text();
        p1Points.setFont(new Font(10));
        p1Points.setTextAlignment(TextAlignment.LEFT);
        p1Points.setText("Player 1 points: " + p1.getPoints());

        // VBox that contains all the players points
        VBox playerPoints = new VBox(20);

        // Goes through each bot to check if and adds them to the VBox
        if (AI.size() == 1) {
            Text botOnePoints = new Text();
            botOnePoints.setFont(new Font(10));
            botOnePoints.setWrappingWidth(0);
            botOnePoints.setTextAlignment(TextAlignment.LEFT);
            botOnePoints.setText("Player 2 points: " + AI.get(0).getPoints());

            playerPoints.getChildren().addAll(totalPoints, p1Points, botOnePoints);
            playerPoints.setAlignment(Pos.CENTER_LEFT);
        }
        else if (AI.size() == 2)    {
            Text botOnePoints = new Text();
            botOnePoints.setFont(new Font(10));
            botOnePoints.setTextAlignment(TextAlignment.LEFT);
            botOnePoints.setText("Player 2 points: " + AI.get(0).getPoints());

            Text botTwoPoints = new Text();
            botTwoPoints.setFont(new Font(10));
            botTwoPoints.setTextAlignment(TextAlignment.LEFT);
            botTwoPoints.setText("Player 3 points: " + AI.get(1).getPoints());

            playerPoints.getChildren().addAll(totalPoints, p1Points, botOnePoints, botTwoPoints);
            playerPoints.setAlignment(Pos.CENTER_LEFT);
        }
        else if (AI.size() == 3)    {
            Text botOnePoints = new Text();
            botOnePoints.setFont(new Font(10));
            botOnePoints.setTextAlignment(TextAlignment.LEFT);
            botOnePoints.setText("Player 2 points: " + AI.get(0).getPoints());

            Text botTwoPoints = new Text();
            botTwoPoints.setFont(new Font(10));
            botTwoPoints.setTextAlignment(TextAlignment.LEFT);
            botTwoPoints.setText("Player 3 points: " + AI.get(1).getPoints());

            Text botThreePoints = new Text();
            botThreePoints.setFont(new Font(10));
            botThreePoints.setTextAlignment(TextAlignment.LEFT);
            botThreePoints.setText("Player 4 points: " + AI.get(2).getPoints());

            playerPoints.getChildren().addAll(totalPoints, p1Points, botOnePoints, botTwoPoints, botThreePoints);
            playerPoints.setAlignment(Pos.CENTER_LEFT);
        }

        VBox playerBids = updateBidVbox(p1, AI);
        VBox trumpVbox = updateTrumpVbox();

        VBox totalVbox = new VBox(50, playerBids, playerPoints, trumpVbox);
        totalVbox.setAlignment(Pos.CENTER_LEFT);
        gamePane.setLeft(totalVbox);


    }

    // Updates when the players make a new bid
    public VBox updateBidVbox (Player p1, ArrayList<AIPlayer> AI)  {
        Text totalBid = new Text();
        totalBid.setFont(new Font(20));
        totalBid.setTextAlignment(TextAlignment.LEFT);
        totalBid.setText("Current Bids");

        // Adds the player
        Text p1Bid = new Text();
        p1Bid.setFont(new Font(10));
        p1Bid.setTextAlignment(TextAlignment.LEFT);
        p1Bid.setText("Player 1 bid: " + p1.getBid());

        // VBox that conatins all the players bids
        VBox playerBids = new VBox(20);

        // Goes through each bot to check if and adds them to the VBox
        if (AI.size() == 1) {
            Text botOneBid = new Text();
            botOneBid.setFont(new Font(10));;
            botOneBid.setTextAlignment(TextAlignment.LEFT);
            botOneBid.setText("Player 2 bid: " + AI.get(0).getBid());

            playerBids.getChildren().addAll(totalBid, p1Bid, botOneBid);
            playerBids.setAlignment(Pos.CENTER_LEFT);
        }
        else if (AI.size() == 2)    {
            Text botOneBid = new Text();
            botOneBid.setFont(new Font(10));
            botOneBid.setTextAlignment(TextAlignment.LEFT);
            botOneBid.setText("Player 2 bid: " + AI.get(0).getBid());

            Text botTwoBid = new Text();
            botTwoBid.setFont(new Font(10));
            botTwoBid.setTextAlignment(TextAlignment.LEFT);
            botTwoBid.setText("Player 3 bid: " + AI.get(1).getBid());

            playerBids.getChildren().addAll(totalBid, p1Bid, botOneBid, botTwoBid);
            playerBids.setAlignment(Pos.CENTER_LEFT);
        }
        else if (AI.size() == 3)    {
            Text botOneBid = new Text();
            botOneBid.setFont(new Font(10));
            botOneBid.setTextAlignment(TextAlignment.LEFT);
            botOneBid.setText("Player 2 bid: " + AI.get(0).getBid());

            Text botTwoBid = new Text();
            botTwoBid.setFont(new Font(10));
            botTwoBid.setTextAlignment(TextAlignment.LEFT);
            botTwoBid.setText("Player 3 bid: " + AI.get(1).getBid());

            Text botThreeBid = new Text();
            botThreeBid.setFont(new Font(10));
            botThreeBid.setTextAlignment(TextAlignment.LEFT);
            botThreeBid.setText("Player 4 bid: " + AI.get(2).getBid());

            playerBids.getChildren().addAll(totalBid, p1Bid, botOneBid, botTwoBid, botThreeBid);
            playerBids.setAlignment(Pos.CENTER_LEFT);
        }
        return playerBids;
    }

    public VBox updateTrumpVbox ()   {
        VBox trumpVbox = new VBox(20);
        trumpVbox.setAlignment(Pos.CENTER_LEFT);

        Text trumpText = new Text();
        trumpText.setFont(new Font(20));
        trumpText.setTextAlignment(TextAlignment.LEFT);
        trumpText.setText("Trump");

        if (trump == 'S')   {
            Image spadeTrump = new Image("file:src/pics/spade.png");
            ImageView s = new ImageView(spadeTrump);
            s.setFitHeight(40);
            s.setFitWidth(40);
            s.setPreserveRatio(true);

            Text spade = new Text();
            spade.setFont(new Font(10));
            spade.setTextAlignment(TextAlignment.LEFT);
            spade.setText("Spade");

            trumpVbox.getChildren().addAll(trumpText, s, spade);
            trumpVbox.setAlignment(Pos.CENTER_LEFT);
        }
        else if (trump == 'C')  {
            Image clubTrump = new Image("file:src/pics/club.png");
            ImageView c = new ImageView(clubTrump);
            c.setFitHeight(40);
            c.setFitWidth(40);
            c.setPreserveRatio(true);

            Text club = new Text();
            club.setFont(new Font(10));
            club.setTextAlignment(TextAlignment.LEFT);
            club.setText("Club");

            trumpVbox.getChildren().addAll(trumpText, c, club);
            trumpVbox.setAlignment(Pos.CENTER_LEFT);
        }
        else if (trump == 'D')  {
            Image diamondTrump = new Image("file:src/pics/diamond.png");
            ImageView d = new ImageView(diamondTrump);
            d.setFitHeight(40);
            d.setFitWidth(40);
            d.setPreserveRatio(true);

            Text diamond = new Text();
            diamond.setFont(new Font(10));
            diamond.setTextAlignment(TextAlignment.LEFT);
            diamond.setText("Diamond");

            trumpVbox.getChildren().addAll(trumpText, d, diamond);
            trumpVbox.setAlignment(Pos.CENTER_LEFT);
        }
        else if (trump == 'H')  {
            Image heartTrump = new Image("file:src/pics/heart.png");
            ImageView h = new ImageView(heartTrump);
            h.setFitHeight(40);
            h.setFitWidth(40);
            h.setPreserveRatio(true);

            Text heart = new Text();
            heart.setFont(new Font(10));
            heart.setTextAlignment(TextAlignment.LEFT);
            heart.setText("Heart");

            trumpVbox.getChildren().addAll(trumpText, h, heart);
            trumpVbox.setAlignment(Pos.CENTER_LEFT);
        }
        else    {
            Text noTrump = new Text();
            noTrump.setFont(new Font(10));
            noTrump.setTextAlignment(TextAlignment.LEFT);
            noTrump.setText("No Trump Played");

            trumpVbox.getChildren().addAll(trumpText, noTrump);
            trumpVbox.setAlignment(Pos.CENTER_LEFT);
        }

        return trumpVbox;
    }


}





















































