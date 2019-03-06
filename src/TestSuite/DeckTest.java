
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeckTest {

    Deck testDeck = new Deck();

    @BeforeAll
    static void startJfx ()    {
        System.out.println("Starting Test ...");
        new JFXPanel();
    }

    @AfterAll
    static void endMessage ()   {
        System.out.println("Ending Test");
    }

    @Test
    void testConstructor () {
        assertNotEquals(null, testDeck, "Testing if Deck was made correctly");
    }

    @Test
    void addAllCards(){
        testDeck.addAllCards();
        assertEquals(52, testDeck.getCards().size());
    }

    @Test
    void addCard() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        testDeck.addCard(c1);
        assertEquals(c1, testDeck.getCards().get(0), "Testing if card was added to deck");
    }

    @Test
    void removeCard() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        testDeck.addCard(c1);
        testDeck.removeCard(c1);
        assertEquals(0, testDeck.getCards().size());
    }

    @Test
    void drawCard() {
        testDeck.addAllCards();
        Card c1 = testDeck.drawCard();
        assertNotEquals(null, c1, "Testing if a card was drew from the deck");
    }

    @Test
    void deleteDeck() {
        testDeck.addAllCards();
        testDeck.deleteDeck();
        assertEquals(0, testDeck.getCards().size(), "Testing if deck was deleted");
    }

    @Test
    void shuffleDeck() {
        testDeck.addAllCards();
        Deck newDeck = new Deck();
        newDeck.addAllCards();
        assertEquals(newDeck.getCards().get(0).getRank(), testDeck.getCards().get(0).getRank(),
                "Testing first card before shuffle");
        testDeck.shuffleDeck();
        assertNotEquals(newDeck.getCards().get(0).getRank(), testDeck.getCards().get(0).getRank(),
                "Testing the first card after shuffle");
    }

    @Test
    void resetDeck() {
        Deck newDeck = new Deck();
        newDeck.addAllCards();
        testDeck.addAllCards();
        assertEquals(newDeck.getCards().get(0).getRank(), testDeck.getCards().get(0).getRank(),
                "Testing if they have the same order");
        testDeck.resetDeck();
        assertNotEquals(newDeck.getCards().get(0).getRank(), testDeck.getCards().get(0).getRank(),
                "Testing if they have the same order which they shouldn't");
    }

    @Test
    void startDeck() {
        Deck newDeck = new Deck();
        newDeck.addAllCards();
        testDeck.startDeck();
        assertNotEquals(newDeck, testDeck, "Testing if startDeck added cards and shuffled");
    }

    @Test
    void getCards() {
        testDeck.addAllCards();
        assertNotEquals(null, testDeck.getCards(), "Testing if getCards returns cards");
    }

    @Test
    void setCards() {
        ArrayList<Card> cardList = new ArrayList<Card>();
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        cardList.add(c1);
        testDeck.setCards(cardList);
        assertEquals(1, testDeck.getCards().size());
    }
}