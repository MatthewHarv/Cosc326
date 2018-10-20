import java.util.Arrays;
import java.util.Random;



public class AIBasicPlayer implements Player {
  
    @Override
    public Action takeTurn(State s, int[] dice) {
         int DiceScore=0;        
         int gooddice=0;

       
         for(int i=0;i<dice.length;i++){
             DiceScore+=dice[i];
             if(dice[i]>4){
                 gooddice++;
             }
             
         }
         if(gooddice<2){
         //    System.out.println("Bad dice give up");
             return Action.FOLD;
         }
         if(gooddice>4){
           //  System.out.println("Great hand roll!");
              return Action.SHOWDOWN;
         }
        
         return Action.ROLL;
    }

    @Override
    public Action actAtShowdown(State s, int[] dice) {
         int DiceScore=0;        
          for(int i=0;i<dice.length;i++){
             DiceScore+=dice[i];
            
         
         }
          
          //Dice score <17 seems to give best result
          if (DiceScore<17){
              return Action.EXIT;
          }
          
        return Action.STAY;
    }

}
