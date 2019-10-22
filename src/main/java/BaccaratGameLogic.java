import java.util.ArrayList;

public class BaccaratGameLogic {
    private int playerPoints;
    private int bankerPoints;
    private int handPoints;

    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2) {
        playerPoints = handTotal(hand1);
        bankerPoints = handTotal(hand2);
        if(playerPoints > bankerPoints && playerPoints <= 9) {
            return "Player";
        }
        else if(playerPoints == bankerPoints) {
            return "Draw";
        }
        else {
            return "Banker";
        }
    }

    public int handTotal(ArrayList<Card> hand) {
        int total = 0;
        for(int i = 0; i < hand.size(); i++) {
            total += hand.get(i).getValue();
            if(total >= 10) {
                total -= 10;
            }
            if(total >= 20) {
                total -= 20;
            }
            if(total >= 30) {
                total -= 30;
            }
         }
        return total;
    }

    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {
        handPoints = handTotal(hand);
        if(handPoints == 0 || handPoints == 1 || handPoints == 2) {
            return true;
        }
        else if (handPoints >= 7){
            return false;
        }
        else {
            if(handPoints <= 5 && playerCard == null) {
                return true;
            }
            else if(handPoints <= 3 && playerCard.getValue() <= 1 || playerCard.getValue() >= 10) {
                return true;
            }
            else if(handPoints <= 4 && playerCard.getValue() == 2 || playerCard.getValue() == 3) {
                return true;
            }
            else if(handPoints <= 5 && playerCard.getValue() == 4 || playerCard.getValue() == 5) {
                return true;
            }
            else if(handPoints <= 6 && playerCard.getValue() == 6 || playerCard.getValue() == 7) {
                return true;
            }
            else if(handPoints <= 2 && playerCard.getValue() == 8 || playerCard.getValue() == 9) {
                return true;
            }
            else if(handPoints <= 3 && playerCard.getValue() == 9) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public boolean evaluatePlayerDraw(ArrayList<Card> hand) {
        handPoints = handTotal(hand);
        return handPoints <= 5;
    }
}
