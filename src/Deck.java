import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    ArrayList<Card> cards;


    Deck()  {
        cards = new ArrayList<Card>();
    }


    // Loops through all the pngs in playingcards and adds them into the deck
    public void addAllCards()   {
        // Opens to the folder and adds all the pngs to an array
        File dir = new File ("src/playingcards/");
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null)   {
            for (File child : directoryListing) {
                String srcOfCard = child.getName();
                // Skip the back of the card png
                if (srcOfCard.equals("gray_back.png")) { continue;}
                String cardRank;
                char cardSuit = '0';

                // If the card is a 10, it will have 3 chars instead of two. This if statement makes sure that
                // the 10 cards will have the right rank
                if ((srcOfCard.charAt(1) - '0') == 0)    {
                    cardRank = "" + srcOfCard.charAt(0) + "" + srcOfCard.charAt(1);
                    cardSuit = srcOfCard.charAt(2);
                    //System.out.println("If, src: " + srcOfCard);
                }   else   {
                    cardRank = "" + srcOfCard.charAt(0);
                    cardSuit = srcOfCard.charAt(1);
                    //System.out.println("else, src: " + srcOfCard);
                }
                // Makes the child the right src of the file
                String src = "file:" + child;
                Card c1 = new Card (cardRank, cardSuit, src, Card.cardValue(cardRank));
                c1.getCardButton().setDisable(true);
                cards.add(c1);
            }
        }
    }

    // Adds a card to the arraylist
    public void addCard (Card c1)   {
        cards.add(c1);
    }

    // Removes a card from the arraylist
    public void removeCard (Card c1)    {
        cards.remove(c1);
    }

    // "Draws" card from deck, removes the card and returns it
    public Card drawCard () {
        Card c1 = cards.get(0);
        cards.remove(0);
        return c1;
    }

    // Removes all cards in the arraylist
    public void deleteDeck ()   {
        cards.clear();
    }

    // Shuffles the cards arraylist
    public void shuffleDeck ()  {
        Collections.shuffle(cards);
    }

    // Combination of functions under one name
    public void resetDeck ()    {
        deleteDeck();
        addAllCards();
        shuffleDeck();
    }

    // Basic command to add all 52 cards into a deck and shuffles it
    public void startDeck ()    {
        addAllCards();
        shuffleDeck();
    }

    public ArrayList<Card> getCards() { return cards;}

    public void setCards (ArrayList<Card> newCards) { cards = newCards;}
}
