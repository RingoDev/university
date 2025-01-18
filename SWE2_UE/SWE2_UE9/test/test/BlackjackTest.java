package test;

import blackjack.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlackjackTest {

    Blackjack game;

    @BeforeEach
    void setUp() {
        game = new Blackjack();
    }

    @Test
    public void testDeckStartsEmpty() {
        assertEquals(0, game.getDeck().size());
    }

    @Test
    public void testDeckFillsUp() {
        game.shuffle();
        assertEquals(52, game.getDeck().size());
    }

    @Test
    public void testEvaluateCardsPlayerWins(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(0));
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(10));
        game.getDealer().addCard(new Card(5));
        game.getDealer().addCard(new Card(4));
        game.distributeWinnings();
        //Human has BJ Dealer has 21;
        assertEquals(GameResult.PlayerWins,game.evaluateCards());
        assertEquals(12,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsPlayerWins2(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(10));
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(8));
        game.getDealer().addCard(new Card(7));
        game.getDealer().addCard(new Card(13));
        game.getDealer().addCard(new Card(0));
        game.distributeWinnings();
        //Human has 20(10,J) Dealer has 19 (9,8,A,A);
        assertEquals(GameResult.PlayerWins,game.evaluateCards());

        assertEquals(11,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsPlayerWins3(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(10));
        game.getDealer().addCard(new Card(5));
        game.getDealer().addCard(new Card(8));
        game.distributeWinnings();
        //Human has 10 Dealer has 25 (busted);
        assertEquals(GameResult.PlayerWins,game.evaluateCards());
        assertEquals(11,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsDealerWins1(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(5));
        game.getHumanPlayer().addCard(new Card(4));
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(10));
        game.getDealer().addCard(new Card(0));
        game.distributeWinnings();
        //Human has 21 Dealer has BJ;
        assertEquals(GameResult.DealerWins,game.evaluateCards());
        assertEquals(9,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsDealerWins2(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(5));
        game.getHumanPlayer().addCard(new Card(6));
        game.getHumanPlayer().addCard(new Card(8));
        game.getDealer().addCard(new Card(6));
        game.distributeWinnings();
        //Human has 22 (busted) Dealer has 7;
        assertEquals(GameResult.DealerWins,game.evaluateCards());
        assertEquals(9,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsDealerWins3(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(10));
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(11));
        game.getDealer().addCard(new Card(5));
        game.getDealer().addCard(new Card(4));
        game.distributeWinnings();
        //Human has 20 Dealer has 21;
        assertEquals(GameResult.DealerWins,game.evaluateCards());
        assertEquals(9,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsDraw1(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(0));
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(13));
        game.getDealer().addCard(new Card(25));
        game.distributeWinnings();
        //Human has BJ Dealer has BJ;
        assertEquals(GameResult.Draw,game.evaluateCards());
        assertEquals(10,game.getHumanPlayer().getChips());
    }
    @Test
    public void testEvaluateCardsDraw2(){
        game.setStartingChips();
        game.setWager();
        game.getHumanPlayer().addCard(new Card(20));
        game.getHumanPlayer().addCard(new Card(9));
        game.getDealer().addCard(new Card(10));
        game.getDealer().addCard(new Card(4));
        game.getDealer().addCard(new Card(2));
        game.distributeWinnings();
        //Human has 18 Dealer has 18;
        assertEquals(GameResult.Draw,game.evaluateCards());
        assertEquals(10,game.getHumanPlayer().getChips());
    }
    @Test
    public void testDrawCards(){
        game.shuffle();
        //drawing 52 cards -> Deck is empty
        for(int i = 0; i< Blackjack.TOTAL_NUM_CARDS ; i++){
            game.drawCard();
        }
        assertThrows(OutOfCardsException.class,() -> game.drawCard());
    }
}
