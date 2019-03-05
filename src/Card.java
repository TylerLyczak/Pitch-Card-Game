import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.*;

import java.awt.event.ActionEvent;

public class Card {
    String rank;
    char suit;
    String src;
    String srcOfBack = "file:src/playingcards/gray_back.png";
    int points;
    Image pic;
    int playedBy;
    ImageView cardPic;
    Button cardButton;





    Card (String rank, char suit, String src, int points)  {
        this.rank = rank;
        this.suit = suit;
        this.src = src;
        this.points = points;

        pic = new Image(src);
        cardPic = new ImageView(pic);
        cardPic.setFitHeight(120);
        cardPic.setFitWidth(90);
        cardPic.setPreserveRatio(true);
        cardButton = new Button();
        cardButton.setGraphic(cardPic);

        /*

        cardButton.setOnAction((event) -> {
            System.out.println("Card Button");
            System.out.println(src);
            //actionPerformed(event);
        });

        cardButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("First Handle");
                System.out.println(event.getClass());
                actionPerformed(event);
                Player.actionPerformed(event);
            }
        });
        */
    }

    Card (Card c1) {
        this.rank = c1.rank;
        this.suit = c1.suit;
        this.src = c1.src;
        this.points = c1.points;
        this.pic = c1.pic;
        cardPic = c1.cardPic;
        cardButton = c1.cardButton;
    }

    public void setPlayedBy (int num) { playedBy = num;}

    public int getPoints () { return points;}

    public char getSuit ()  { return suit;}

    public int getPlayedBy () { return playedBy;}

    public String getRank ()    { return rank;}

    public Button getCardButton ()    { return cardButton;}

    public void makeButton()    {
        cardPic = new ImageView(pic);
        cardPic.setFitHeight(500);
        cardPic.setFitWidth(300);
        cardPic.setPreserveRatio(true);
        cardButton = new Button();
        cardButton.setGraphic(cardPic);
    }

    // Used for calculating game point
    public static int cardValue (String rank)   {
        switch (rank)    {
            case "10":
                return 10;
            case "J":
                return 1;
            case "Q":
                return 2;
            case "K":
                return 3;
            case "A":
                return 4;
            default:
                return 0;
        }
    }

    // Helper function for cardSuitDecider, used for calculating high and low trump
    public static int cardRank (String rank)   {
        switch (rank)   {
            case "A":
                return 14;
            case "K":
                return 13;
            case "Q":
                return 12;
            case "J":
                return 11;
            case "10":
                return 10;
            case "9":
                return 9;
            case "8":
                return 8;
            case "7":
                return 7;
            case "6":
                return 6;
            case "5":
                return 5;
            case "4":
                return 4;
            case "3":
                return 3;
            case "2":
                return 2;
            default:
                return 0;
        }
    }

    // Used for calculating high and low trump in calculateScore()
    public static boolean cardSuitDecider (String rank1, String rank2)  {
        if (rank1.equals(rank2))    { return false;}

        if (cardRank(rank1) > cardRank(rank2))  { return false;}
        else    { return true;}
    }

    public Card removeAndReturn ()  {
        Card c1 = this;
        return c1;
    }

    public void actionPerformed (javafx.event.ActionEvent event) {
        System.out.println("wow");
    }



}
