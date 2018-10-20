/**
 * Represents a single turn for a player in Summit (for logging purposes).
 * 
 * @author Michael Albert
 */
public class Turn {
    
    Action a;
    int playerIndex;
    int roll = 0; 

    /**
     * A representation of a particular non-ROLL turn for a player. 
     * 
     * @param a The action of this turn (not ROLL)
     * @param playerIndex The index of the player
     */
    public Turn(Action a, int playerIndex) {
        this.a = a;
        this.playerIndex = playerIndex;
    }
    
    /**
     * A representation of a ROLL turn for a player. The roll will be positive
     * if accepted and negative if rejected. For example, a roll of -3 would
     * mean that 3 was rolled and rejected (so the player's minimum die was at
     * least a 3).
     * 
     * @param playerIndex
     * @param roll 
     */
    Turn(int playerIndex, int roll) {
        this.a = Action.ROLL;
        this.playerIndex = playerIndex;
        this.roll = roll;
    }
    
    public String toString() {
        return "Player " + playerIndex + " " + a + ((roll != 0) ? (" " + roll) : "");
    }

}
