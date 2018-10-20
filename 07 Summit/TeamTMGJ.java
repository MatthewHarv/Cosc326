

public class TeamTMGJ implements Player {

    public static int roundCounter = 0;
    public static boolean startingRound = true;
    public static int ourPosition;
    public static int numberOfPlayers;
    public static int[][] estimatedPlayersHands;

    public void analyseData(State s) {
        estimatedPlayersHands = new int[s.bets.length][5];
        for (int a = 0; a < s.log.size(); a++) {
            String[] splitInfo = s.log.get(a).toString().split(" ");
            Integer playerIndex = Integer.parseInt(splitInfo[1]);
            Integer playerRoll = Integer.parseInt(splitInfo[3]);
            if (playerRoll < 0&&playerIndex!=ourPosition) {
                for (int i = 0; i < 5; i++) {
                    if (estimatedPlayersHands[playerIndex][i] < Math.abs(playerRoll)) {
                        if(roundCounter<2){
                               estimatedPlayersHands[playerIndex][i] = Math.abs(playerRoll);
                        }
                        else if(roundCounter<4){
                             estimatedPlayersHands[playerIndex][i] = Math.abs(playerRoll)+1;
                        }            
                        else{
                        estimatedPlayersHands[playerIndex][i] = Math.abs(playerRoll)+3;
                    }
                    }
                }
            }
            if (playerRoll > 0) {
                int smallestValue = Integer.MAX_VALUE;
                int smallestIndex = 0;
                for (int i = 0; i < 5; i++) {
                    if (estimatedPlayersHands[playerIndex][i] < smallestValue) {
                        smallestValue = estimatedPlayersHands[playerIndex][i];
                        smallestIndex = i;
                    }
                }
                estimatedPlayersHands[playerIndex][smallestIndex] = playerRoll;
            }
        }
        for (int col = 0; col < s.bets.length; col++) {
            for (int row = 0; row < 5; row++) {
                if (estimatedPlayersHands[col][row] == 0) {
                  
                        estimatedPlayersHands[col][row] = 4;                  
                }
                }
            
        }
      
        
        
    }

    @Override
    public Action takeTurn(State s, int[] dice) {
        if (startingRound) {
            roundSetup(s);
            startingRound = false;
        }

        analyseData(s);
        roundCounter++;
        int DiceScore = 0;
        int gooddice = 0;

        for (int i = 0; i < dice.length; i++) {
            DiceScore += dice[i];
            if (dice[i] > 4) {
                gooddice++;
            }

        }
        int topScore = 0;
        int score = 0;
        int currentEnemyGoodDice=0;
        int totalEnemyGoodDice=0;
        for (int col = 0; col < s.bets.length; col++) {
            if (col != ourPosition) {
                for (int row = 0; row < 5; row++) {
                    score += estimatedPlayersHands[col][row];
                    if(estimatedPlayersHands[col][row]>4){
                        currentEnemyGoodDice++;
                    }
                }

            }

            if (score > topScore) {
                topScore = score;
                totalEnemyGoodDice=currentEnemyGoodDice;
            }
            score = 0;
            currentEnemyGoodDice=0;
        }
        if (topScore > DiceScore + 10||gooddice<2||totalEnemyGoodDice>gooddice+3) {
              reset();
            return Action.FOLD;
        }
        if (topScore < DiceScore - 10||gooddice==5||totalEnemyGoodDice+4<gooddice) {
            return Action.SHOWDOWN;
        }
        return Action.ROLL;
    }

    @Override
    public Action actAtShowdown(State s, int[] dice) {
        analyseData(s);
        int DiceScore = 0;
        for (int i = 0; i < dice.length; i++) {
            DiceScore += dice[i];
        }
        for (int col = 0; col < s.bets.length; col++) {
            int score = 0;
            if (col != ourPosition) {
                for (int row = 0; row < 5; row++) {
                    score += estimatedPlayersHands[col][row];
                }
                if (score > DiceScore) {
                    reset();
                   return Action.EXIT;
                }

            }
        }

        reset();
        return Action.STAY;
    }

    public static void roundSetup(State st) {
        numberOfPlayers = st.bets.length;
        int smallest = Integer.MAX_VALUE;
        for (int i = 0; i < st.bets.length; i++) {
            if (st.bets[i] > 0 && smallest > st.bets[i]) {
                smallest = st.bets[i];
            }
        }
        ourPosition = smallest;

    }

    public static void reset() {
        startingRound = true;
        roundCounter = 0;
    }

}
