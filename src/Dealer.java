import java.util.ArrayList;

public interface Dealer {
    Deck gameDeck = new Deck();

    public ArrayList<Card> dealHand();

    public Deck getGameDeck();

}
