import java.util.Arrays;
import java.util.Random;

/**
 * A fairly random player
 * @author Michael Albert
 */
public class AIRandomPlayer implements Player {
    
    private static Random R = new Random();

    @Override
    public Action takeTurn(State s, int[] dice) {
     //   System.out.println("AI Dice: " + Arrays.toString(dice));
        switch (R.nextInt(3)) {
            case 0:
                return Action.ROLL;
            case 1:
                return Action.SHOWDOWN;
            default:
                return Action.FOLD;
        }
    }

    @Override
    public Action actAtShowdown(State s, int[] dice) {
        switch (R.nextInt(2)) {
            case 0:
                return Action.STAY;
            default:
                return Action.EXIT;
        }
    }

}
