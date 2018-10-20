/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class WinCalculator {
    int player0wins=0;
    int player1wins=0;
    int player2wins=0;
    int player3wins=0;
    
    public void winner(int x){
        if(x==0){
            player0wins++;
        }
        if(x==1){
            player1wins++;
        }
        if(x==2){
            player2wins++;
        }
        if(x==3){
            player3wins++;
        }
    System.out.println("Hi winner "+ x);
}
    
    public void scoreboard(){
        System.out.println("--scores--");
        System.out.println("Player 0 score = "+player0wins);
        System.out.println("Player 1 score = "+player1wins);
        System.out.println("Player 2 score = "+player2wins);
        System.out.println("Player 3 score = "+player3wins);
    }
}
