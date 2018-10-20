import java.util.*;


class DNA{
    /*
    direction of next move based on direction of previous,
    mapped to int, N = 0, E = 1, S = 2, W = 3
    */
    public int[] direction_map = new int[4];

    /*
    State to leave square in after movement based on previous direction
    N = 0, E = 1, S = 2, W = 3, the corresponding state is found at this index
    */
    public char[] state_update = new char[4];

    public DNA(int[] move, char [] state ){

        this.direction_map = move;
        this.state_update = state;
    }

    public void debug(){
        System.out.println("");
        System.out.println("");
        System.out.println("Direction Map");
        System.out.print("[");
        for(int i = 0; i < direction_map.length; i++){
            if(i != 0){
                System.out.print("\t");
            }
            System.out.print(direction_map[i]);
        }
        System.out.println("]\n");

        System.out.println("State Update");
        System.out.print("[");

        for(int i = 0; i < state_update.length; i++){
            if(i != 0){
                System.out.print("\t");
            }
            System.out.print(state_update[i]);
        }
        System.out.println("]\n");

    }


}
public class Ants{

    /*
        Hashmap of coordinates that are "keyyed" by string concatenation of x and y coordinates,
        the value at each hash is the state that, that square of the grid is currently in
    */
    public HashMap<String, Character> grid = new HashMap<String, Character>();

    /*
        Map of state character with it's individual DNA the array of direction and state changes to
        implement.
    */
    public Map<Character, DNA> state = new HashMap<Character, DNA>();

    public char init_state;


    /*
    Uses all raw lines read from the Scanner that were not comments or the step count,
    and creates a piece of DNA.
    */
    public void add_states(ArrayList<String> raw){
        for(int i = 0; i < raw.size(); i++){
            char state = raw.get(i).charAt(0); // state "ID"

            int[] move_map = new int[4];
            char[] state_map = new char[4];

            move_map[0] = char_to_int(raw.get(i).charAt(2)); // arrived from North
            move_map[1] = char_to_int(raw.get(i).charAt(3)); // arrived from East
            move_map[2] = char_to_int(raw.get(i).charAt(4)); // arrived from South
            move_map[3] = char_to_int(raw.get(i).charAt(5)); // arrived from West

            state_map[0] = raw.get(i).charAt(7); // State point is left after arrival from North
            state_map[1] = raw.get(i).charAt(8); // State point is left after arrival from East
            state_map[2] = raw.get(i).charAt(9); // State point is left after arrival from South
            state_map[3] = raw.get(i).charAt(10); // State point is left after arrival from West

            // Init new DNA
            DNA d = new DNA(move_map, state_map);

            this.state.put(state, d);
            if(i == 0){ // Store the inital state of the entire grid so that when grid element is empty we can use this
                this.init_state = state;
            }
        }
    }
    /*
    map char directions to 0,1,2,3
    which also works well for indices of array
    */
    public int char_to_int(char direction){
        if(direction == 'N'){
            return 0;
        }
        if(direction == 'E'){
            return 1;
        }
        if(direction == 'S'){
            return 2;
        }
        return 3;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        while(sc.hasNext()){
            ArrayList<String> raw_input = new ArrayList<String>();
            boolean keep_reading = true;
            int number_of_steps = 0;
            while(keep_reading){
                String line = sc.nextLine();
                if(line.length() == 11 && line.charAt(0) != '#'){ // This is a state declaration
                    raw_input.add(line);
                }
                else if(line.charAt(0) != '#'){ // This is number of steps, and final piece of scenario
                    number_of_steps = Integer.parseInt(line);
                    keep_reading = false;
                }
            }


            /* Populate the Ants State */
            Ants A = new Ants();
            A.add_states(raw_input);

            int x = 0;
            int y = 0;
            int direction = 0;

            for(int i = 0; i < number_of_steps; i++){

                /*
                Used for updating the state after we have left the square
                */
                int orig_direction = direction;
                int orig_x = x;
                int orig_y = y;

                //
                Get state letter from grid hashmap
                Character state = A.grid.get(Integer.toString(x) + "_" + Integer.toString(y));

                /*
                If there is no state already set then we haven't been here yet, and the square
                should be initalised as the inital state (line 1)
                */
                if(state == null){
                    state = A.init_state;
                }
                DNA d = A.state.get(state);
                //d.debug();

                direction = d.direction_map[direction];

                /* Update (or overwrite) the state at the specific location on the grid */
                A.grid.put(Integer.toString(orig_x) + "_" + Integer.toString(orig_y), d.state_update[orig_direction]);
                if(direction == 0){
                    y++;
                }
                else if(direction == 1){
                    x++;
                }
                else if(direction == 2){
                    y--;
                }
                else if(direction == 3){
                    x--;
                }
            }
            for(int i = 0; i < raw_input.size(); i++){
                System.out.println(raw_input.get(i));
            }
            System.out.println("# " + x + " " + y);
            if(sc.hasNext()){
                System.out.println();
            }
        }
    }
}
