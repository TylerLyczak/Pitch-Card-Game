import java.util.ArrayList;
import java.util.Random;

public class PitchDealer implements DealerType{

    Dealer gameDealer;
    Random randomGenerator;

    @Override
    public Dealer createDealer() {
        gameDealer.gameDeck.startDeck();
        randomGenerator = new Random();
        return gameDealer;
    }

    // This pulls 6 cards from the deck with a random number generator and adds it to an arraylist
    public ArrayList<Card> dealHand()   {
        if (gameDealer.gameDeck.getCards().size() < 6)   {
            // Reload deck
            // Get rid of any remaining cards in anyones hand
        }
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i=0; i<6; i++) {
            // Gets a random number depending on the size of the deck.
            int ranIndex = randomGenerator.nextInt(gameDealer.gameDeck.getCards().size());
            Card c1 = gameDealer.gameDeck.drawCard();
            hand.add(c1);
        }
        return hand;
    }

    public int deckSize ()  {
        return gameDealer.gameDeck.getCards().size();
    }

    public void dealerReset ()  {
        gameDealer.gameDeck.resetDeck();
    }


    public void checkDeck (int playerNum)   {
        if (gameDealer.gameDeck.getCards().size() < 6 || gameDealer.gameDeck.getCards().size() < playerNum)  {
            dealerReset();
        }
    }
}
