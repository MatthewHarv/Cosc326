/*
 * COSC326 2018 Etude 11: HeadsAndTails
 * Carl Aiau
 * Matthew Harvey
 *
 */
import java.util.*;

public class HeadsAndTails{
    private char[] coins;
    private int heads;
    private int tails;
    private int moves;
    private boolean efficiency_test;
    private boolean equal_t_h;
    private long num_actions;

    /* Constructor */
    public HeadsAndTails(int heads, int tails, boolean efficiency_test){
        this.heads = heads;
        this.tails = tails;
        this.efficiency_test = efficiency_test;
        num_actions = 0;
        moves = 0;
        if(heads == tails){
            coins = new char[(heads * 2)  + 4];
            /* Init coins */
            for (int i = 0; i < coins.length; i++) {
                if(heads > 0){
                    coins[i] = 'H';
                    heads--;
                } else if(tails > 0){
                    coins[i] = 'T';
                    tails--;
                } else{
                    coins[i] = '_';
                }

                if(efficiency_test){
                    num_actions++;
                }
            }
        } else{
            coins = new char[(heads + tails)  + 2];
            coins[0] = '_';
            coins[1] = '_';
            for (int i = 2; i < coins.length; i++) {
                if(heads > 0){
                    coins[i] = 'H';
                    heads--;
                } else if(tails > 0){
                    coins[i] = 'T';
                    tails--;
                } else{
                    coins[i] = '_';
                }
                if(efficiency_test){
                    num_actions++;
                }
            }
        }

    }
    public int get_moves(){
        return moves;
    }
    public long get_num_actions(){
        return num_actions;
    }

    public void print_info(){
        print_coins();
        System.out.println("\n");
    }
    /* Prints all the coins out
    public void print_indexs(){
        for(int i = 0; i < this.coins.length; i++){
            System.out.print(i % 10 + " ");
        }
        System.out.println("");
    }
    */
    public void print_coins(){
        for(int i = 0; i < this.coins.length; i++){
                System.out.print(this.coins[i] + " ");
        }
        System.out.println("");
    }

    /*
    We felt it was very important to keep track of
    indexes rather than continously iterating over the array
    */
    public void solve(){

        /* head n, tails n */
        if(heads == tails){
            int setup = 2 * heads;
            int to = 2 * heads - 1;
            int from = heads - 1;

            /* intial Setup */
            print_info();
            coins[setup] = coins[0];
            coins[0] = '_';
            coins[setup + 1] = coins[1];
            coins[1] = '_';
            print_info();
            coins[setup + 2] = coins[setup - 1];
            coins[setup - 1] = '_';
            coins[setup + 3] = coins[setup];
            coins[setup] = '_';
            moves += 2;
            print_info();

            while(from >= 2){

                coins[to] = coins[from];
                coins[to + 1] = coins[from + 1];
                coins[from] = '_';
                coins[from + 1] = '_';
                moves++;
                to -= 2;
                print_info();
                if(from > 2){
                    moves++;
                    coins[from] = coins[to];
                    coins[to] = '_';
                    coins[from + 1] = coins[to + 1];
                    coins[to + 1] = '_';
                    print_info();
                }
                from--;
            }
        }

        else{ // This is n, n - 1
            int from = heads + 1;
            int to = 0;
            if(!efficiency_test)
                print_info();
            while(from < coins.length - 1){
                // Moving left
                coins[to] = coins[from];
                coins[to + 1] = coins[from + 1];
                coins[from] = '_';
                coins[from + 1] = '_';
                to += 2;
                moves++;
                if(!efficiency_test)
                    print_info();

                // Moving right
                if(from == coins.length - 2){
                    break;
                }
                coins[from] = coins[to];
                coins[from + 1] = coins [to + 1];
                coins[to] = '_';
                coins[to + 1] = '_';
                from++;
                moves++;
                if(!efficiency_test)
                    print_info();

            }

        }





    }



    /*
        Application class!
        Where we do the instantiating and deal with the user input and output
    */
    public static void main(String[] args){
        HeadsAndTails hat;
        Scanner sc = new Scanner(System.in);
        int heads;
        /* Create Command Line instructions */
        System.out.println("\nHEADS & TAILS\n=============");
        System.out.println("Choose your task, via keypad:");
        System.out.println("1: Three heads followed by three tails.");
        System.out.println("2: Three heads followed by two tails.");
        System.out.println("3: Write a program to find the number of moves to" +
        " alternate n heads and n tails for 2 < n < 20.");
        System.out.println("4: Extend your program to handle n heads and n − 1 tails.");
        System.out.println("5: Efficiency Test.");

        while(sc.hasNext()){
            int choice = 6;
            try{
                choice = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
            }
            /* Switch on choice of user input */
            switch(choice){
                case 1: /* Corresponds to Etude Task 2 */
                    heads = 3;
                    System.out.println(heads + " heads, " + ( heads) + " tails");
                    hat = new HeadsAndTails(heads, heads, false);
                    hat.solve();
                    System.out.println("Moves: " + hat.get_moves() + "\n");
                    break;

                case 2: /* Corresponds to Etude Task 2 */
                    heads = 3;
                    System.out.println(heads + " heads, " + ( heads - 1 ) + " tails");
                    hat = new HeadsAndTails(heads, heads -1, false);
                    hat.solve();
                    System.out.println("Moves: " + hat.get_moves() + "\n");
                    break;

                case 3:
                    for(heads = 3; heads < 20; heads++){
                        System.out.println("\n" + heads +" heads, " + (heads) + " tails");
                        hat = new HeadsAndTails(heads, heads, false);
                        hat.solve();
                        System.out.println("Moves: " + hat.get_moves() );
                    }
                    break;
                case 4:
                    System.out.println("Please Enter Number of Heads");
                    heads = Integer.parseInt(sc.nextLine());
                    System.out.println("Please Enter Number of tails (only heads, or -1 heads supported)");
                    int tails = Integer.parseInt(sc.nextLine());
                    System.out.println( heads +" heads, " + (tails) + " tails");
                    hat = new HeadsAndTails(heads, tails, false);
                    hat.solve();
                    System.out.println("Moves: " + hat.get_moves() );
                    break;
                case 5:
                    for(heads = 3; heads < 1000000; heads += 10){
                        int n = (heads * 2 - 1);
                        hat = new HeadsAndTails(heads, heads - 1, true);
                        hat.solve();
                        System.out.println(n + ", " + hat.get_moves() + ", " + hat.get_num_actions());
                    }
                    break;

                default:
                    /* Redisplay Instructions if number not 1 - 5 input */
                    System.out.println("Choose your task, via keypad:");
                    System.out.println("1: Three heads followed by three tails.");
                    System.out.println("2: Three heads followed by two tails.");
                    System.out.println("3: Write a program to find the number of moves to" +
                    " alternate n heads and n tails for 2 < n < 20.");
                    System.out.println("4: Extend your program to handle n heads and n − 1 tails.");
                    System.out.println("5: Efficiency Test.");
                    break;
            }
        }
    }
}
