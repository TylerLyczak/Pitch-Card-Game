import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest {

    AIPlayer testBot = new AIPlayer();

    @BeforeAll
    static void startMessage () {
        new JFXPanel();
        System.out.println("Starting Test...");
    }

    @AfterAll
    static void endMessage ()   {
        System.out.println("Ending Test");
    }

    @Test
    void testConstructor () {
        assertNotEquals(null, testBot.getRandomGenerator(), "Test if randomGenerator was initialized");
        assertNotEquals(null, testBot.getHand(), "Test if hand was initialized");
        assertNotEquals(null, testBot.getTrickDeck(), "Tests if trickDeck was initialized");
    }

    // Case 1 is where no cards were played at all, so new round
    // Bot will play its highest suit card
    @Test
    void playCardCase1Test1() {
        char trump = 'S';
        ArrayList<Character> suits = new ArrayList<Character>();
        ArrayList<Card> trickList = new ArrayList<Card>();
        Card c1 = new Card("J", 'S', "file:../playingcards/JS.png", 11);
        Card c2 = new Card("3", 'S', "file:../playingcards/3S.png", 3);
        Card c3 = new Card("Q", 'S', "file:../playingcards/QS.png", 12);
        Card c4 = new Card("5", 'S', "file:../playingcards/5S.png", 5);
        Card c5 = new Card("10", 'S', "file:../playingcards/10S.png", 10);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.getHand().addCard(c5);
        ArrayList<Card> originalHand = (ArrayList<Card>)testBot.getHand().getCards().clone();
        testBot.playCard(trickList, suits, 1, trump);
        originalHand.removeAll(testBot.getHand().getCards());
        assertEquals(c3.getRank(), originalHand.get(0).getRank(), "Test first case, should return Queen");
    }

    // Case 1 is where no cards were played at all, so new round
    // Bot will play its highest suit card of its highest suit
    @Test
    void playCardCase1Test2 ()   {
        char trump = 'S';
        ArrayList<Character> suits = new ArrayList<Character>();
        ArrayList<Card> trickList = new ArrayList<Card>();
        Card c1 = new Card("J", 'S', "file:../playingcards/JS.png", 11);
        Card c2 = new Card("3", 'S', "file:../playingcards/3S.png", 3);
        Card c3 = new Card("Q", 'S', "file:../playingcards/QS.png", 12);
        Card c4 = new Card("A", 'C', "file:../playingcards/AC.png", 5);
        Card c5 = new Card("10", 'S', "file:../playingcards/10S.png", 10);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.getHand().addCard(c5);
        ArrayList<Card> originalHand = (ArrayList<Card>)testBot.getHand().getCards().clone();
        testBot.playCard(trickList, suits, 1, trump);
        originalHand.removeAll(testBot.getHand().getCards());
        assertEquals(c4.getRank(), originalHand.get(0).getRank(), "Test first case, should return Ace of Clubs");
    }

    @Test
    void determineBid() {
    }
}