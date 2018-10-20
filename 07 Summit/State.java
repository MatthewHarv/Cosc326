import java.util.ArrayList;
import java.util.HashMap;

/**
 * A container class that represents the state of a Summit game in progress. 
 * The players are represented by indices 0, 1, 2, ... corresponding to their
 * initial bets (so player 0 leads the action). When players leave the game the
 * other players' indices are unchanged.
 * 
 * @author Michael Albert
 */
class State {
    
    int pot;                                // Amount in the pot
    int maxBet;                             // The current maximum bet
    ArrayList<Integer> playersRemaining;    // The indices of the remaining players
    int[] bets;                             // The bets of  all the players. Players who have folded will
                                            // have negative (or zero) bets indicating the amount they added to the pot.
    ArrayList<Turn> log;                    // The game to this point
        
    /**
     * The bet required from the current player to stay in.
     * @return the amount the current player must bet (total) to stay in
     * the round.
     */
    int betRequired() {
        return maxBet+1;
    }
    
    /**
     * The rolls of one of the remaining players. These are represented
     * as positive integers for rolls accepted, and negative ones for rolls
     * rejected.
     * 
     * @param i the index of a player
     * @return the rolls that player accepted or rejected in order
     */   
    ArrayList<Integer> getRolls(int i) {
        ArrayList<Integer> result = new ArrayList<>();
        for(Turn t : log) {
            if (t.playerIndex == i && t.a == Action.ROLL) result.add(t.roll);
        }
        return result;
    }
    
    /**
     * The current bet of a particular player.
     * @param i the index of the player
     * @return the current bet of that player
     */
    int getBet(int i) {
        return bets[i];
    }

}
