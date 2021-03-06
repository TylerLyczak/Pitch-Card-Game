import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
    private String rank;
    private int rankVal;
    private char suit;
    private String src;
    final String srcOfBack = "file:src/playingcards/gray_back.png";
    private int points;
    private Image pic;
    private int playedBy;
    private ImageView cardPic;
    private Button cardButton;



    Card (String rank, char suit, String src, int points, int rankVal)  {
        this.rank = rank;
        this.suit = suit;
        this.src = src;
        this.points = points;
        this.rankVal = rankVal;

        pic = new Image(src);
        cardPic = new ImageView(pic);
        cardPic.setFitHeight(120);
        cardPic.setFitWidth(90);
        cardPic.setPreserveRatio(true);
        cardButton = new Button();
        cardButton.setGraphic(cardPic);

    }

    Card (Card c1) {
        this.rank = c1.rank;
        this.suit = c1.suit;
        this.src = c1.src;
        this.points = c1.points;
        this.pic = c1.pic;
        this.playedBy = c1.playedBy;
        this.cardPic = c1.cardPic;
        this.cardButton = c1.cardButton;
    }

    public void setPlayedBy (int num) { playedBy = num;}

    public int getPoints () { return points;}

    public char getSuit ()  { return suit;}

    public int getPlayedBy () { return playedBy;}

    public String getRank ()    { return rank;}

    public Button getCardButton ()    { return cardButton;}

    public String getSrc () { return src;}

    public ImageView getCardPic ()  { return cardPic;}

    public int getRankVal ()    { return rankVal;}

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
}
