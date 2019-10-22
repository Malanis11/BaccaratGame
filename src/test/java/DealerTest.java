import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class DealerTest {

	BaccaratDealer dealer;
	BaccaratGameLogic logic;
	Card card;
	ArrayList<Card> player;
	ArrayList<Card> banker;

	@BeforeEach
	void init() {
		dealer = new BaccaratDealer();
		logic = new BaccaratGameLogic();
		card = new Card("Hearts", 1);
	}

	@Test
	void testCardInit() {
		assertEquals("Card", card.getClass().getName(), "Did not initialize Card");
	}

	@Test
	void testWhoWon1() {
		player = new ArrayList<Card>();
		player.add(new Card("Spades", 2));
		player.add(new Card("Diamonds", 6));
		banker.add(new Card("Clubs", 3));
		banker.add(new Card("Hearts", 2));
		assertEquals("Player", logic.whoWon(player, banker), "Wrong person won");
	}
	@Test
	void testWhoWon2() {
		player = new ArrayList<Card>();
		player.add(new Card("Spades", 2));
		player.add(new Card("Diamonds", 2));
		banker.add(new Card("Clubs", 2));
		banker.add(new Card("Hearts", 2));
		assertEquals("Draw", logic.whoWon(player, banker), "Did not evaluate to draw");
	}

	@Test
	void testHandTotal1() {
		player = new ArrayList<Card>();
		player.add(new Card("Spades", 1));
		player.add(new Card("Hearts", 8));
		assertEquals(9, logic.handTotal(player), "Incorrect hand total");
	}
	@Test
	void testHandTotal2() {
		player = new ArrayList<Card>();
		player.add(new Card("Spades", 10));
		player.add(new Card("Hearts", 11));
		assertEquals(1, logic.handTotal(player), "Incorrect hand total");
	}

	@Test
	void testEvaluateBankerDraw1() {
		banker = new ArrayList<Card>();
		banker.add(new Card("Clubs", 1));
		banker.add(new Card("Diamonds", 1));
		assertTrue(logic.evaluateBankerDraw(banker, card));
	}
	@Test
	void testEvaluateBankerDraw2() {
		banker = new ArrayList<Card>();
		banker.add(new Card("Diamonds", 3));
		banker.add(new Card("Diamonds", 4));
		assertFalse(logic.evaluateBankerDraw(banker, card));
	}

	@Test
	void testEvaluatePlayerDraw1() {
		player = new ArrayList<Card>();
		player.add(new Card("Clubs", 2));
		player.add(new Card("Diamonds", 3));
		assertTrue(logic.evaluatePlayerDraw(player));
	}
	@Test
	void testEvaluatePlayerDraw2() {
		player = new ArrayList<Card>();
		player.add(new Card("Clubs", 4));
		player.add(new Card("Diamonds", 3));
		assertFalse(logic.evaluatePlayerDraw(player));
	}

	@Test
	void testShuffleDeck1() {
		BaccaratDealer dealer2 = dealer;
		dealer2.shuffleDeck();
		assertFalse(dealer.equals(dealer2));
	}
	@Test
	void testShuffleDeck2() {
		dealer.shuffleDeck();
		BaccaratDealer dealer2 = dealer;
		dealer2.shuffleDeck();
		assertFalse(dealer.equals(dealer2));
	}

	@Test
	void testDeckSize1() {
		assertEquals(52, dealer.deckSize(), "Wrong deck size");
	}
	@Test
	void testDeckSize2() {
		dealer.dealHand();
		assertEquals(50, dealer.deckSize(), "Wrong deck size");
	}

}
