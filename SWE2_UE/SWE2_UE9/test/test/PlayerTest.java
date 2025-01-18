package test;

import blackjack.game.Card;
import blackjack.player.Dealer;
import blackjack.player.HumanPlayer;
import blackjack.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    Player human;
    Player dealer;

    @BeforeEach
    void setUp() {
        human = new HumanPlayer();
        dealer = new Dealer();
    }

    @Test
    void testHasBlackjack() {
        //0 == Ace
        human.addCard(new Card(0));
        assertFalse(human.hasBlackJack());
        assertFalse(dealer.hasBlackJack());
        //10 == Jack
        human.addCard(new Card(10));
        assertTrue(human.hasBlackJack());
        assertFalse(dealer.hasBlackJack());
    }

    @Test
    void testPlayerBusted() {

        dealer.addCard(new Card(5));
        //index 5 == 6 ... sum == 6
        assertFalse(dealer.isBusted());

        dealer.addCard(new Card(7));
        //index 7 == 8 ... sum == 14
        assertFalse(dealer.isBusted());

        dealer.addCard(new Card(20));
        //index 20 == 8 ... sum == 22
        assertTrue(dealer.isBusted());
    }

    @Test
    void testAceCount() {
        assertEquals(0, human.aceCount());
        // 0,13,26,39 are Aces ... 15 is not an Ace
        human.addCard(new Card(0));
        assertEquals(1, human.aceCount());

        human.addCard(new Card(13));
        assertEquals(2, human.aceCount());

        human.addCard(new Card(15));
        assertEquals(2, human.aceCount());

        human.addCard(new Card(26));
        assertEquals(3, human.aceCount());

        human.addCard(new Card(39));
        assertEquals(4, human.aceCount());
    }

    @Test
    void testGetValue() {
        human.addCard(new Card(0));
        // cards : A ...value == 11
        assertEquals(11, human.getValue());

        human.addCard(new Card(26));
        // cards : A,A ...value == 12
        assertEquals(12, human.getValue());

        human.addCard(new Card(3));
        // cards : A,A,4 ...value == 16
        assertEquals(16, human.getValue());

        human.addCard(new Card(5));
        // cards : A,A,4,6 ...value == 12
        assertEquals(12, human.getValue());

        human.addCard(new Card(13));
        // cards : A,A,4,6,A ...value == 13
        assertEquals(13,human.getValue());
        assertFalse(human.isBusted());

        human.addCard(new Card(8));
        // cards : A,A,4,6,A,9 ...value == 22
        assertEquals(22,human.getValue());
        assertTrue(human.isBusted());
    }
    @Test
    void testReset(){
        assertEquals(0,human.getCards().size());
        human.addCard(new Card(5));
        assertEquals(1,human.getCards().size());
        human.addCard(new Card(8));
        assertEquals(2,human.getCards().size());
        human.addCard(new Card(2));
        assertEquals(3,human.getCards().size());
        human.reset();
        assertEquals(0,human.getCards().size());
    }
}