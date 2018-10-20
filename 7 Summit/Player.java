/**
 * The interface that represents a Summit player. Note that implementations of
 * this interface will generally make use of the information encapsulated in the
 * State objects that they receive from the game manager to collect and update
 * information about the other players' actions.
 *
 * @author Michael Albert
 */
public interface Player {
    
    
    /**
     * Take action at your turn. Should return one of FOLD, ROLL, or SHOWDOWN.
     * FOLD is considered the default, i.e., if you return any other action it
     * will have the effect of folding.
     *
     * @param s the current state of the game
     * @param dice your current dice (sorted least to greatest)
     * @return the action taken by a player at their turn.
     */
    Action takeTurn(State s, int[] dice);

    /**
     * Take action at a showdown initiated by another player. Should return one
     * of STAY or EXIT. EXIT is considered the default, i.e., if you return any
     * other action it will have the effect of exiting.
     *
     * @param s the current state of the game
     * @param dice your current dice (sorted least to greatest)
     * @return the action taken at showdown.
     */
    Action actAtShowdown(State s, int[] dice);

}
