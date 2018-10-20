
import java.text.NumberFormat;
import java.util.*;

/**
 *
 * @author Michael Albert
 */
public class SummitGame {

    public static int player0money = 0;
    public static int player1money = 0;
    public static int player2money = 0;
    public static int player3money = 0;
    public static int player4money = 0;
    public static int player0position = 0;
    public static int player1position = 0;
    public static int player2position = 0;
    public static int player3position = 0;
    public static int player4position = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 3000000; i++) {
            ArrayList<Player> players = new ArrayList<>();
            int[] mix = randArray();
            int counter = 0;
            for (int j = 0; j < mix.length; j++) {
                if (mix[j] == 0) {
                    // This will be player 0
                    players.add(counter, new AIBasicPlayer());
                    player0position = counter;
                    counter++;
                }
                if (mix[j] == 1) {
                    // This will be player 1
                    players.add(counter, new AISmartPlayer());
                    player1position = counter;
                    counter++;
                }
                if (mix[j] == 2) {
                    // This will be player 2
                    players.add(counter, new TeamTMGJ());
                    player2position = counter;
                    counter++;
                }
                if (mix[j] == 3) {
                    // This will be player 3
                    players.add(counter, new AIRandomPlayer());
                    player3position = counter;
                    counter++;
                }
                if (mix[j] == 4) {
                    // This will be player 4
                    players.add(counter, new AIRollPlayer());
                    player4position = counter;
                    counter++;
                }
            }
            Round r = new Round(players.toArray(new Player[0]));

            player0money += r.scorecount()[player0position];
            player1money += r.scorecount()[player1position];
            player2money += r.scorecount()[player2position];
            player3money += r.scorecount()[player3position];
            player4money += r.scorecount()[player4position];

        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        System.out.println("----- score -------");
        System.out.println("TMGJ v1 won " + formatter.format(player0money));
        System.out.println("TMGJ v2 won " + formatter.format(player1money));
        System.out.println("TMGJ v3 won " + formatter.format(player2money));
        System.out.println("AI Random Player won " + formatter.format(player3money));
        System.out.println("AI Roll Player won " + formatter.format(player4money));

    }

    public static int[] randArray() {
        Random rand = new Random();
        int[] mix = new int[5];
        for (int c = 0; c < mix.length; c++) {
            mix[c] = c;
        }

        for (int j = mix.length - 1; j > 0; --j) {
            int k = rand.nextInt(j + 1);
            int temp = mix[j];
            mix[j] = mix[k];
            mix[k] = temp;
        }
        return mix;
    }
}
