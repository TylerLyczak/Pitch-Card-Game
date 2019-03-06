import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {
    Random randomGenerator;

    AIPlayer () {
        randomGenerator = new Random();
        hand = new Deck();
        trickDeck = new ArrayList<Card>();
    }

    public Random getRandomGenerator ()   { return randomGenerator;}

    public boolean playCard (ArrayList<Card> trickList, ArrayList<Character> suits, int bot, char trump)   {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Makes a new list of cards with only cards that can be played at the time
        ArrayList<Card> selectableCards = new ArrayList<Card>();
        ArrayList<Card> nonSelectableCards = new ArrayList<Card>();
        boolean cardsPlayedRound = false;
        if (suits.size() != 0)  {
            cardsPlayedRound = true;
            // change playable cards
            for (int i=0; i<hand.getCards().size(); i++)    {
                selectableCards.add(hand.getCards().get(i));
                for (int j=0; j<suits.size(); j++)  {
                    if (hand.getCards().get(i).getSuit() == suits.get(j))   {
                        nonSelectableCards.add(hand.getCards().get(i));
                    }
                }
            }
            // Removes all the cards the AI cant play so it follows the rules
            selectableCards.removeAll(nonSelectableCards);
        }

        if (selectableCards.size() == 0)    {
            selectableCards = hand.getCards();
        }
        // Determines how the AI will choose a card
        boolean cardsPlayedTurn = false;
        if (trickList.size() == 0)  { cardsPlayedTurn = false;}
        else    { cardsPlayedTurn = true;}

        // Plays the highest suit card it has
        // Case where the bot is the first to go in the round
        if (!cardsPlayedRound && !cardsPlayedTurn)   {
            System.out.println("Case 1");
            Card highCard = hand.getCards().get(0);
            int index = 0;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (Card.cardRank(highCard.getRank()) < Card.cardRank(hand.getCards().get(i).getRank()))    {
                    highCard = hand.getCards().get(i);
                    index = i;
                }
            }
            System.out.println("Case 1 Success");
            // Plays the card
            hand.getCards().get(index).setPlayedBy(bot);
            trickList.add(hand.getCards().get(index));
            hand.getCards().remove(index);
            return true;
        }
        // Case 2
        // Case where cards were played by a previous trick, bot won trick
        else if (cardsPlayedRound && !cardsPlayedTurn)  {
            System.out.println("Case 2");
            Card highCard = selectableCards.get(0);
            int index = 0;
            for (int i=0; i<selectableCards.size(); i++)    {
                if (Card.cardRank(highCard.getRank()) < Card.cardRank(selectableCards.get(i).getRank()))    {
                    highCard = selectableCards.get(i);
                    index = i;
                }
            }
            int handIndex = -1;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == highCard) {
                    handIndex = i;
                    break;
                }
            }
            System.out.println("Case 2 Success");
            // Plays the card
            hand.getCards().get(handIndex).setPlayedBy(bot);
            trickList.add(hand.getCards().get(handIndex));
            hand.getCards().remove(handIndex);
            return true;
        }

        // else case where the bot is not the first to go
        // Sees if there is a trump in the tricklist
        boolean trumpPlayed = false;
        int trumpRank = 0;
        for (int i=0; i<trickList.size(); i++)  {
            if (trickList.get(i).getSuit() == trump)    {
                trumpPlayed = true;
                if (Card.cardRank(trickList.get(i).getRank()) > trumpRank)  {
                    trumpRank = Card.cardRank(trickList.get(i).getRank());
                }
            }
        }
        // Sees if the bot has a higher trump card to beat the trumps in the tricklist
        boolean hasTrump = false;
        boolean onlyTrumps = true;
        int hasTrumpRank = 0;
        Card hasTrumpCard = null;
        for (int i=0; i<selectableCards.size(); i++)    {
            if (selectableCards.get(i).getSuit() == trump)  {
                hasTrump = true;
                for (int j=0; j<trickList.size(); j++)  {
                    if (Card.cardRank(trickList.get(j).getRank()) > hasTrumpRank)  {
                        hasTrumpRank = Card.cardRank(selectableCards.get(i).getRank());
                        hasTrumpCard = selectableCards.get(i);
                    }
                }
            }
            else    { onlyTrumps = false;}
        }

        // Sees if the bots trump is higher than the tricklist trump high, if not, it will play a less valuable card
        if (trumpPlayed && hasTrump)    {
            System.out.println("Case 3");
            // If the bots trump is higher, than it will play its highest
            if (hasTrumpRank > trumpRank)   {
                int handIndexTrump = -1;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == hasTrumpCard) {
                        handIndexTrump = i;
                        break;
                    }
                }
                System.out.println("Case 3 Success");
                // Playes the card
                hand.getCards().get(handIndexTrump).setPlayedBy(bot);
                trickList.add(hand.getCards().get(handIndexTrump));
                hand.getCards().remove(handIndexTrump);
                return true;
            }
        }

        // Bot wont play its high trump if it doesn't have too, it will play any low non trump card it has
        if (hasTrump && !onlyTrumps)   {
            System.out.println("Case 4");
            Card lowNonTrump = null;
            int lowNonTrumpIndex = -1;
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() != trump)  {
                    lowNonTrump = selectableCards.get(i);
                    lowNonTrumpIndex = i;
                    break;
                }
            }
            // Now that we found the non trump card, we need to make sure its the lowest
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() != trump)   {
                    if (Card.cardRank(lowNonTrump.getRank()) > Card.cardRank(selectableCards.get(i).getRank())) {
                        lowNonTrump = selectableCards.get(i);
                        lowNonTrumpIndex = i;
                    }
                }
            }
            // Gets the index of the low non trump card in the hand
            int handLow = -1;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == lowNonTrump)  {
                    handLow = i;
                    break;
                }
            }
            System.out.println("Case 4 Success");
            // Playes the card
            hand.getCards().get(handLow).setPlayedBy(bot);
            trickList.add(hand.getCards().get(handLow));
            hand.getCards().remove(handLow);
            return true;
        }

        // Case where the bot only has trumps but can't beat the high trump in the tricklist, so it will try to play
        // any of its middle trump cards

        if (onlyTrumps) {
            System.out.println("Case 5");
            Card lowTrump = selectableCards.get(0);
            Card highTrump = selectableCards.get(0);
            Card middleTrump = null;
            int lowTrumpIndex = 0;
            int highTrumpIndex = 0;
            int middleTrumpIndex = -1;
            for (int i=0; i<selectableCards.size(); i++)    {
                if (Card.cardRank(lowTrump.getRank()) > Card.cardRank(selectableCards.get(i).getRank()))    {
                    lowTrump = selectableCards.get(i);
                    lowTrumpIndex = i;
                }
                if (Card.cardRank(highTrump.getRank()) < Card.cardRank(selectableCards.get(i).getRank()))   {
                    highTrump = selectableCards.get(i);
                    highTrumpIndex = i;
                }
            }
            middleTrumpIndex = highTrumpIndex - lowTrumpIndex;
            System.out.println("middleTrump: " + middleTrumpIndex);
            middleTrump = selectableCards.get(middleTrumpIndex);
            // Gets the index of the middle trump in the hand
            int newMiddleIndex = -1;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == middleTrump)  {
                    newMiddleIndex = i;
                    break;
                }
            }
            System.out.println("Case 5 Success");
            // Playes the card
            hand.getCards().get(newMiddleIndex).setPlayedBy(bot);
            trickList.add(hand.getCards().get(newMiddleIndex));
            hand.getCards().remove(newMiddleIndex);
            return true;
        }

        System.out.println("Case 6 Success");
        // Case where all else fails, it will play a random card
        // Never happens but it is safe to have included
        System.out.println("OOOOOOOOOOOOOO SHHIIIIIITTTTTTTTTTT  bot: " + bot);
        int ranIndex = randomGenerator.nextInt(hand.cards.size());
        hand.cards.get(ranIndex).setPlayedBy(bot);
        trickList.add(hand.cards.get(ranIndex));
        hand.cards.remove(ranIndex);
        return true;

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
            else if (weightPercentage.get(i) >= 0.5 && bidToChoose.contains(3))  {
                calcBid.add(3);
            }
            else if (weightPercentage.get(i) >= 0.6 && bidToChoose.contains(3))  {
                calcBid.add(3);
            }
            else if (weightPercentage.get(i) >= 0.65 && bidToChoose.contains(4)) {
                calcBid.add(4);
            }
            else if (weightPercentage.get(i) >= 0 && bidToChoose.contains(1))    {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) >= 0.1 && bidToChoose.contains(1))   {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) >= 0.2 && bidToChoose.contains(1))   {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) >= 0.35 && bidToChoose.contains(2))  {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) >= 0.4 && bidToChoose.contains(2))  {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) >= 0.45 && bidToChoose.contains(3)) {
                calcBid.add(3);
            }
        }

        // For-loop that gets the highest bid placed into the array list
        int finalBid = 0;
        for (int i=0; i<calcBid.size(); i++)    {
            if (calcBid.get(i) > finalBid)  {
                finalBid = calcBid.get(i);
                System.out.println("Final bid: " + finalBid);
            }
        }

        // Weights were not high enough to make a bid
        if (finalBid == 0)  { bid = 1; previousBids.add(1);}
        // Returns the highest bid.
        else    { bid = finalBid; previousBids.add(finalBid);}
    }

}
