import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

public class Player {
    Deck hand;
    ArrayList<Card> trickDeck;
    int bid;
    int newBid;
    int points;
    int tempPoints;
    boolean buttonPress;

    Player ()   {
        hand = new Deck();
        trickDeck = new ArrayList<Card>();
        buttonPress = false;
    }


    public boolean playCard (ArrayList<Card> trick) {
        //boolean pressed = false;
        for (int i=0; i<this.hand.cards.size(); i++)    {
            //System.out.println(hand.cards.get(i).cardButton);
            hand.cards.get(i).cardButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (int i=0; i<hand.cards.size(); i++) {
                        if (hand.cards.get(i).cardButton == event.getSource())  {
                            hand.cards.get(i).setPlayedBy(1);
                            //System.out.println("YEEET");
                            //Update hbox
                            hand.cards.get(i).cardButton.setVisible(false);
                            trick.add(hand.cards.get(i));
                            hand.cards.remove(i);
                            buttonPress = true;
                            break;
                        }
                    }
                }
            });
        }
        return buttonPress;
    }

    public void resetBid () { bid = 0;}

    public void changeBoolButtonPress () { buttonPress = false;}

    public ArrayList<Card> getTrickDeck ()  { return trickDeck;}

    public int getBid ()    { return bid;}

    public Deck getHand ()  { return hand;}

    public int getPoints () { return points;}

    public int getTempPoints () { return tempPoints;}

    public void setHand (Deck newHand)  { hand = newHand;}

    public void giveNewHand (ArrayList<Card> newHand)   { hand.setCards(newHand);}

    public void incrementPoints (int amount)   { points += amount;}

    public void decrementPoints (int amount)    { points -= amount;}

    public void incrementTempPoints (int amount)    { tempPoints += amount;}

    public void resetTempPoints ()  { tempPoints = 0;}

    public void setBid (int amount) { bid = amount; newBid = amount;}


    // Makes all the cards in a players hand not clickable
    public void changeCardDisability (boolean bool) {
        for (int i=0; i<hand.getCards().size(); i++)    {
            hand.getCards().get(i).getCardButton().setDisable(bool);
        }
    }
}
