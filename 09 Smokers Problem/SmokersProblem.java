
import java.util.*;

/**
 * COSC326, Etude 9: Smoker's Problem
 *
 * Finds the minimum distance of a smoker to a non smoker, and the total
 * distance the smoker must travel to reach their destination.
 *
 * @author Gus, James, Matthew, Theo
 */
public class SmokersProblem {

    /**
     * The Node class defines and gives actions to a grid.
     */
    private class Node {

        public List<Node> neighbours = new ArrayList();
        public Node parent = null;
        public int column;
        public int row;
        public boolean isValid = true;

        /**
         * Default Constructor: Sets the row and column of a node.
         *
         * @param r
         * @param c
         */
        public Node(int r, int c) {
            this.row = r;
            this.column = c;
        }

        /**
         * Finds the cost of the node.
         *
         * The cost equals the nodes distance from the start plus the straight
         * line distance from the node to the finish.
         *
         * @return The cost of the node.
         */
        public double getCost() {
            double h = Math.sqrt(Math.pow(((grid[this.row].length - 1) - this.column), 2)
                    + Math.pow(((grid.length - 1) - this.row), 2));
            return h + this.row + this.column;
        }

        @Override
        public String toString() {
            String result = "Node(" + this.row + ", " + this.column + ")";
            return result;
        }
    }

    private final Node[][] grid;
    private ArrayList<ArrayList<Node>> nonSmokers;
    private ArrayList<Node> newNonSmokers;

    /**
     * Default constructor: builds the bounds of the grid.
     *
     * @param M
     * @param N
     */
    public SmokersProblem(int M, int N) {
        this.grid = new Node[M][N];
    }

    /**
     * Main Method Makes a grid of Nodes from the input.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int M = -1, N = -1; // M = number of rows, N = number of columns
        ArrayList<Integer[]> nonSmokers = new ArrayList<>();
        while (scan.hasNextLine()) {
            String myString = scan.nextLine();
            if (myString.length() == 0) { // blank line
                if (M != -1 && N != -1 && !nonSmokers.isEmpty()) {
                    SmokersProblem smoke = new SmokersProblem(M, N);
                    smoke.setUp(nonSmokers);
                    smoke.SmokersSearch();
                    // Reset the variables
                    M = -1;
                    N = -1;
                    nonSmokers = new ArrayList();
                } else {
                    break;
                }
            } else {
                // Split up the input
                String s[] = myString.split(" ", 2);
                if (M == -1 || N == -1) {
                    M = Integer.parseInt(s[0]);
                    N = Integer.parseInt(s[1]);
                } else {
                    Integer[] nonSmoker = new Integer[2];
                    nonSmoker[1] = Integer.parseInt(s[0]);
                    nonSmoker[0] = Integer.parseInt(s[1]);
                    nonSmokers.add(nonSmoker);
                }
            }
        }
        if (M != -1 && N != -1 && !nonSmokers.isEmpty()) {
            SmokersProblem smoke = new SmokersProblem(M, N);
            smoke.setUp(nonSmokers);
            smoke.SmokersSearch();
        }
    }

    /**
     * Builds the grid.
     *
     * @param nonSmokers The non smoker nodes
     */
    private void setUp(ArrayList<Integer[]> nonSmokers) {
        this.nonSmokers = new ArrayList();
        // Builds the grid
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = new Node(r, c);
            }
        }
        // Makes the nodes that are non smokers invalid
        for (Integer[] nonSmoker : nonSmokers) {
            ArrayList<Node> non = new ArrayList();
            non.add(new Node(nonSmoker[0], nonSmoker[1]));
            this.nonSmokers.add(non);
            grid[nonSmoker[0]][nonSmoker[1]].isValid = false;
        }
        // Adds the neighbours to the nodes
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                Node n = grid[r][c];
                List<Node> neighbours = n.neighbours;
                if (r > 0) {
                    neighbours.add(grid[r - 1][c]);
                }
                if (r < grid.length - 1) {
                    neighbours.add(grid[r + 1][c]);
                }
                if (c > 0) {
                    neighbours.add(grid[r][c - 1]);
                }
                if (c < grid[r].length - 1) {
                    neighbours.add(grid[r][c + 1]);
                }
                n.neighbours = neighbours;
            }
        }
    }

    /**
     * Finds the best path for the smoker. The best path is the one where the
     * smoker is as far away from each non smoker as possible.
     *
     * Builds walls around the non smokers, expanding them as far as possible
     * before it is impossible to find a path.
     */
    private void SmokersSearch() {
        ArrayList<Node> path = getPath();
        ArrayList<Node> next_path;
        int M = 1; // The distance from the furthest non smoker
        int true_M = -1; // The minimum distance from the closest non smoker 
        int[] mins = new int[nonSmokers.size()];
        for (int m : mins) {
            m = 0;
        }
        boolean flag = true;
        while (flag && M < grid.length + grid[0].length) {
            flag = false;
            for (int i = 0; i < nonSmokers.size(); i++) {
                expandWalls(nonSmokers.get(i));
                next_path = getPath();
                if (next_path == null) {
                    if (true_M == -1) {
                        true_M = M;
                    }
                    if (mins[i] == 0) {
                        mins[i] = M;
                    }
                    undoExpandWalls(nonSmokers.get(i));
                } else {
                    path = next_path;
                    flag = true;
                }
            }
            M++;
        }
        int T = 0;
        int i = 0;
        for (int m : mins) {
            // Hack that fixes some cases
            if (m == 0) {
                Node x = nonSmokers.get(i).get(0);
                T += x.column - 1;
            } else {
                T += m;
            }
            i++;
        }

        if (path != null) {
            System.out.println("min " + true_M + ", total " + T);
        }
    }
    
    /**
     * Uses A star search to find the best path from the start to finish.
     *
     * Thanks to https://gist.github.com/raymondchua/8064159. which helped me to
     * write this program.
     *
     * @return The list of nodes that make up the best path
     */
    private ArrayList<Node> getPath() {
        HashSet<Node> explored = new HashSet();
        ArrayList<Node> path = new ArrayList<>();
        ArrayList<Node> true_path = new ArrayList<>();

        int nodes = grid.length * grid[0].length;
        PriorityQueue<Node> queue = new PriorityQueue<>(nodes, (Node i, Node j) -> {
            if (i.getCost() > j.getCost()) {
                return 1;
            } else if (i.getCost() < j.getCost()) {
                return -1;
            } else {
                return 0;
            }
        });

        Node start = grid[0][0];
        for (ArrayList<Node> nonSmoker : nonSmokers) {
            for (Node n : nonSmoker) {
                if (n.row == 0 && n.column == 0) {
                    return null;
                }
            }
        }
        Node finish = grid[grid.length - 1][grid[0].length - 1];
        for (ArrayList<Node> nonSmoker : nonSmokers) {
            for (Node n : nonSmoker) {
                if (n.row == finish.row && n.column == finish.column) {
                    return null;
                }
            }
        }
        Node current = start;
        path.add(current);
        queue.add(current);

        while (current != finish && !queue.isEmpty()) {
            for (Node n : current.neighbours) {
                if (!explored.contains(n)) {
                    if (n.isValid) {
                        n.parent = current;
                        queue.add(n);
                    }
                }
            }

            int i = 1;
            explored.add(current);
            current = queue.poll();
            path.add(current);
        }
        if (current == finish) {
            Node n = path.get(path.size() - 1);
            while (n != start) {
                true_path.add(n);
                n = n.parent;
            }
            true_path.add(start);
            return true_path;
        } else {
            return null;
        }
    }

    /**
     * Expands the walls around the non smoker node.
     *
     * @param nodes The non smoker node
     */
    private void expandWalls(ArrayList<Node> nodes) {
        this.newNonSmokers = new ArrayList(); // The nodes that make up the walls
        int i = 0;
        while (i < nodes.size()) {
            Node node = nodes.get(i);
            for (Node n : grid[node.row][node.column].neighbours) {
                if (grid[n.row][n.column].isValid) {
                    grid[n.row][n.column].isValid = false;
                    Node non = new Node(n.row, n.column);
                    newNonSmokers.add(non);
                }
            }
            i++;
        }
        for (Node n : newNonSmokers) {
            nodes.add(n); // builds the walls
        }
    }

    /**
     * Reverse the expandWalls method.
     *
     * @param nodes The non smoker node
     */
    private void undoExpandWalls(ArrayList<Node> nodes) {
        // Resets the previous built nodes
        for (Node n : newNonSmokers) {
            nodes.remove(n);
            grid[n.row][n.column].isValid = true;
        }
        this.newNonSmokers = new ArrayList();
    }

    /**
     * Prints the grid with the path from start to finish.
     *
     * @param path The path of the grid
     */
    private void printPath(ArrayList<Node> path) {
        for (Node[] nodes : grid) {
            for (Node node : nodes) {
                if (path.contains(node)) {
                    System.out.print("+");
                } else if (node.isValid) {
                    System.out.print("o");
                } else {
                    System.out.print("x");
                }
            }
            System.out.println();
        }
    }
}

// End
