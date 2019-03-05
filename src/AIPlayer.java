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
        /*
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        int ranIndex = randomGenerator.nextInt(hand.cards.size());
        hand.cards.get(ranIndex).setPlayedBy(bot);
        trick.add(hand.cards.get(ranIndex));
        hand.cards.remove(ranIndex);
        return true;
    }

    public boolean playCard2 (ArrayList<Card> trickList, ArrayList<Character> suits, int bot)   {
        if (suits.size() != 0)  {
            // change playable cards
            for (int i=0; i<hand.getCards().size(); i++)    {

            }
        }


        return false;
    }

    public void makeBid (int highBid)  {
        //int ranIndex = randomGenerator.nextInt();
        ArrayList<Integer> nums = new ArrayList<Integer>();
        nums.add(1);
        for (int i=2; i<6; i++) {
            if (i > highBid)    {
                nums.add (i);
            }
        }
        int ranIndex = randomGenerator.nextInt(nums.size());
        bid = nums.get(ranIndex);
    }

    // Determines what bid the bot will make depending on their hand
    public void determineBid (ArrayList<Integer> previousBids) {
        // 0 = Diamonds
        // 1 = Clubs
        // 2 = Hearts
        // 3 = Spades
        ArrayList<Integer> suitWeights = new ArrayList<Integer>();
        int diamondWeight = 0;
        int clubWeight = 0;
        int heartWeight = 0;
        int spadeWeight = 0;
        // This for-loop goes through the bots hand and adds up the value of all the cards of each suit into separate weights
        for (int i=0; i<hand.getCards().size(); i++)    {
            if (hand.getCards().get(i).getSuit() == 'D')    {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    diamondWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'C')   {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    clubWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'H')   {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    heartWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'S')   {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    spadeWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
        }

        suitWeights.add(diamondWeight);
        suitWeights.add(clubWeight);
        suitWeights.add(heartWeight);
        suitWeights.add(spadeWeight);
        System.out.println("DiamondWeight: " + diamondWeight);
        System.out.println("ClubWeight: " + clubWeight);
        System.out.println("HeartWeight: " + heartWeight);
        System.out.println("SpadeWeight: " + spadeWeight);

        // If any of the weights are above 60, then they have a really good change of winning the game, making
        // them bid smudge.
        if (diamondWeight > 60 || clubWeight > 60 || heartWeight > 60 || spadeWeight > 60)  {
            bid = 5;
        }

        int weightTotal = diamondWeight + clubWeight + heartWeight + spadeWeight;

        // Makes a percentage Array List to see the probability of each suit
        ArrayList<Double> weightPercentage = new ArrayList<Double>();
        weightPercentage.add( (double)diamondWeight/ (double)weightTotal);
        weightPercentage.add ( (double)clubWeight/ (double)weightTotal);
        weightPercentage.add ( (double)heartWeight/ (double)weightTotal);
        weightPercentage.add ( (double)spadeWeight/ (double)weightTotal);


        ArrayList<Integer> bidToChoose = new ArrayList<Integer>();
        bidToChoose.add(1);
        // Gets the highest bid to determine what the bot can choose from
        // If the previous bid is a 5 or smudge, then its bid will automatically be 0
        int highBid = 0;
        for (int i=0; i<previousBids.size(); i++)   {
            if (previousBids.get(i) == 5)   {
                bid = 1;
                return;
            }
            if (highBid < previousBids.get(i))  {
                highBid = previousBids.get(i);
            }
        }

        // Since one is already added, it starts at 2
        for (int i=2; i<6; i++)    {
            if (highBid < i)    {
                bidToChoose.add(i);
            }
        }

        // This for loop loops through each percentage and determines what bid it should do based on its percentage.
        ArrayList<Integer> calcBid = new ArrayList<Integer>();
        for (int i=0; i<weightPercentage.size(); i++)   {
            System.out.println("Percentage: " + weightPercentage.get(i));
            if (weightPercentage.get(i) > 0.5 && bidToChoose.contains(2))   {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) > 0.5 && bidToChoose.contains(3))  {
                calcBid.add(3);
            }
            else if (weightPercentage.get(i) > 0.6 && bidToChoose.contains(3))  {
                calcBid.add(3);
            }
            else if (weightPercentage.get(i) > 0.65 && bidToChoose.contains(4)) {
                calcBid.add(4);
            }
            else if (weightPercentage.get(i) > 0 && bidToChoose.contains(1))    {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) > 0.1 && bidToChoose.contains(1))   {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) > 0.2 && bidToChoose.contains(1))   {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) > 0.35 && bidToChoose.contains(2))  {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) > 0.4 && bidToChoose.contains(2))  {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) > 0.45 && bidToChoose.contains(3)) {
                calcBid.add(3);
            }
        }

        // For-loop that gets the highest bid placed into the array list
        int finalBid = 0;
        for (int i=0; i<calcBid.size(); i++)    {
            if (calcBid.get(i) > finalBid)  {
                finalBid = calcBid.get(i);
            }
        }

        // Weights were not high enough to make a bid
        if (finalBid == 0)  { bid = 1; previousBids.add(1);}
        // Returns the highest bid.
        else    { bid = finalBid; previousBids.add(finalBid);}
    }

}
