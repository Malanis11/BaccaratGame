import java.util.ArrayList;
import java.util.Collections;

public class BaccaratDealer {
    private ArrayList<Card> deck;
    private int[] cardVal = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    private String[] cardSuite = {"Spades", "Hearts", "Clubs", "Diamonds"};

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void generateDeck() {
        deck = new ArrayList<Card>();
        for(int i = 0; i < 13; i++) {
            for(int j = 0; j < 4; j++) {
                deck.add(new Card(cardSuite[j], cardVal[i]));
                Collections.shuffle(deck);
            }
        }
    }

    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<Card>();
        if(deck.size() >= 6) {
            for (int i = 0; i < 2; i++) {
                hand.add(deck.get(0));
                deck.remove(0);
            }
        }
        return hand;
    }

    public Card drawOne() {
        Card drawn;
        if(deck.size() >= 6) {
            drawn = deck.get(0);
            deck.remove(0);
            return drawn;
        }
        else {
            generateDeck();
            Collections.shuffle(deck);
            drawn = deck.get(0);
            return drawn;
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public int deckSize() {
        return deck.size();
    }
}
