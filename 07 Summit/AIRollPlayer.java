
import java.util.Arrays;

/**
 *
 * @author Michael Albert
 */
public class AIRollPlayer implements Player {

    @Override
    public Action takeTurn(State s, int[] dice) {
        // System.out.println("AI Dice: " + Arrays.toString(dice));
        return Action.ROLL;
    }

    @Override
    public Action actAtShowdown(State s, int[] dice) {
        return Action.STAY;
    }

}
