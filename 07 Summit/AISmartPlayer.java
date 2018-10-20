import java.util.Arrays;
import java.util.Random;


public class AISmartPlayer implements Player {
  public int roundCounter=0;
  public static int i=0;
    @Override
    public Action takeTurn(State s, int[] dice) {
        if(i==0){
            System.out.println("The start");
            i++;
        }
        roundCounter++;
         int DiceScore=0;        
         int gooddice=0;

       
         for(int i=0;i<dice.length;i++){
             DiceScore+=dice[i];
             if(dice[i]>4){
                 gooddice++;
             }
             
         }
         
         if(roundCounter==1){
             if(gooddice<2){
                return Action.FOLD;
            }   
         }
         else if(roundCounter==3){
             if(gooddice<3){
                return Action.FOLD;
            }
         } 
          else if(roundCounter==14){
             if(gooddice<4){
                return Action.FOLD;
            }
         }   
          else if(roundCounter==21){
             if(gooddice<5){
                return Action.FOLD;
            }
         }          
         else if(roundCounter>5){
             if(gooddice>4){
                  return Action.SHOWDOWN;
             }
         }
   
         return Action.ROLL;
    }

    @Override
    public Action actAtShowdown(State s, int[] dice) {
        
        if(s.playersRemaining.size()==1){
      //      System.out.println("last");
        }
      //  System.out.println(s.playersRemaining.size());
         int DiceScore=0;        
          for(int i=0;i<dice.length;i++){
             DiceScore+=dice[i];        
         }

          if (DiceScore<17){
              return Action.EXIT;
          }
          
        return Action.STAY;
    }

}
