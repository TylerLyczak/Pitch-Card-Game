import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {
    Random randomGenerator;

    AIPlayer () {
        randomGenerator = new Random();
        hand = new Deck();
        trickDeck = new ArrayList<Card>();
        System.out.println("Bot Added");
    }


    public boolean playCard (ArrayList<Card> trick, int bot)    {
        System.out.println("RAnd time");
        System.out.println(hand.cards.size());
        int ranIndex = randomGenerator.nextInt(hand.cards.size());
        System.out.println(ranIndex);
        hand.cards.get(ranIndex).setPlayedBy(bot);
        trick.add(hand.cards.get(ranIndex));
        hand.cards.remove(ranIndex);
        return true;
    }
}
