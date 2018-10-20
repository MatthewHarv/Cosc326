import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * One round of the Summit game.
 *
 * @author Michael Albert
 */
class Round {
    public int winnernumber=0;
    public int winneramount=0;
   
    public int[] moneyChange;
    
    private Player[] players;
    private int[][] dice;
    private State s = new State();
    private static Random R = new Random();

     Round(Player[] players) {
        this.players = players;

        initialiseDice();
        initialiseState();

        int winner = playRound();

    //  System.out.println(Arrays.toString(s.bets));
      //  System.out.println("Player " + winner + " collects " + s.pot);
        winnernumber=winner;
        winneramount=s.pot;
        moneyChange=s.bets;
        
        moneyChange[winner]=s.pot;

        
    }
     public int[] scorecount(){
    return moneyChange;
}
  

    private void initialiseDice() {
        dice = new int[players.length][5];

        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < 5; j++) {
                dice[i][j] = R.nextInt(6) + 1;
            }
            Arrays.sort(dice[i]);
        }
    }

    private void initialiseState() {
        s.pot = 0;
        s.maxBet = players.length - 1;
        s.playersRemaining = new ArrayList<>();
        s.bets = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            s.playersRemaining.add(i);
            s.bets[i] = i;
        }

        s.log = new ArrayList<>();
    }

    private int playRound() {
        int player = s.playersRemaining.remove(0);

        // If everyone but the current player has folded it's like a showdown.
        if (s.playersRemaining.isEmpty()) {
            return doShowdown(player);
        }
        Action a = players[player].takeTurn(s, dice[player]);
        switch (a) {
            case ROLL:
                doRoll(player);
                s.playersRemaining.add(player);
                break;
            case SHOWDOWN:
                return doShowdown(player);
            case FOLD:
            default:
                doFold(player);
                break;
        }
        return playRound();
    }

    private void doRoll(int player) {
        doBet(player);
        int roll = R.nextInt(6) + 1;
        if (roll > dice[player][0]) {
            dice[player][0] = roll;
            Arrays.sort(dice[player]);
        } else {
            roll = -roll;
        }
        log(new Turn(player, roll));
    }

    private void doFold(int player) {
        int b = (s.bets[player] + s.playersRemaining.size() - 1);
        b /= s.playersRemaining.size();
        s.pot += b;
        s.bets[player] = -b;
    }

    private void doBet(int player) {
        s.bets[player] = s.betRequired();
        s.maxBet = s.bets[player];
    }

    private int doShowdown(int player) {
        doBet(player);
        for (int p : s.playersRemaining) {
            Action a = players[p].actAtShowdown(s, dice[p]);
            switch (a) {
                case STAY:
                    break;
                case EXIT:
                default:
                    int b = (s.bets[p] + 1) / 2;
                    s.bets[p] = -b;
                    s.pot += b;
            }
        }
        int winner = -1;
        int maxTotal = -1;
        for (int i = 0; i < players.length; i++) {
            if (s.bets[i] > 0) {
                int sum = 0;
                for (int d : dice[i]) {
                    sum += d;
                }
      //         System.out.println("Player " + i + " total: " + sum);
                if (sum > maxTotal || (sum == maxTotal && s.bets[i] > s.bets[winner])) {
                    if (winner >= 0) {
                        s.pot += s.bets[winner];
                        s.bets[winner] = -s.bets[winner];
                    }
                    winner = i;
                    maxTotal = sum;
                } else {
                    s.pot += s.bets[i];
                    s.bets[i] = -s.bets[i];
                }
            } else {
       //        System.out.println("Player " + i + " is not in final showdown");
            }
        }
        return winner;
    }

    private void log(Turn turn) {
  //     System.out.println(turn);
        s.log.add(turn);
    }

}
