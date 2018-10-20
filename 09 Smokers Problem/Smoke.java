
import java.util.*;

/**
 *
 *
 */
public class Smoke {

    public static ArrayList<Integer> nonSmokersCoordinates;

    private class Node {

        public List<Node> neighbours;
        public int column;
        public int row;
        public boolean isValid;

        public Node(int r, int c) {
            this.row = r;
            this.column = c;
            this.neighbours = new ArrayList<>();
            this.isValid = true;
        }

        @Override
        public String toString() {
            String result = "Node(" + this.row + ", " + this.column + ")";
            return result;
        }

        private double getH() {
            double result = Math.sqrt(Math.pow(((grid[this.row].length - 1) - this.column), 2)
                    + Math.pow(((grid.length - 1) - this.row), 2));
            return result;
        }
    }

    public Node[][] grid;
    public ArrayList<Integer[]> nonSmokers;

    public Smoke(int M, int N) {
        this.grid = new Node[M][N];
    }

    /**
     * Main Method Makes a grid of Nodes.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int M = 0, N = 0;
        ArrayList<Integer[]> nonSmokers = new ArrayList<>();
        nonSmokersCoordinates = new ArrayList<>();
        int countinput = 0;
        while (scan.hasNext()) {
            String myString = scan.nextLine();
            String s[] = myString.split(" ", 2);
            if (myString.length() == 0) {
                countinput = 0;
             
                Smoke smoke = new Smoke(M, N);
                smoke.nonSmokers = nonSmokers;
                smoke.setUp(nonSmokers);
                smoke.SmokersSearch();
                 nonSmokers = new ArrayList<>();
                 nonSmokersCoordinates = new ArrayList<>();
                  M = 0; 
                  N = 0;
            } else {
                if (countinput == 0) {
                    M = Integer.parseInt(s[0]);
                    N = Integer.parseInt(s[1]);
                    countinput++;
                } else {
                    Integer[] n = new Integer[2];
                    nonSmokersCoordinates.add(Integer.parseInt(s[0]));
                    nonSmokersCoordinates.add(Integer.parseInt(s[1]));
                    n[0] = Integer.parseInt(s[0]);
                    n[1] = Integer.parseInt(s[1]);
                    nonSmokers.add(n);
                }
            }
        }
       
        Smoke smoke = new Smoke(M, N);
        smoke.nonSmokers = nonSmokers;
        smoke.setUp(nonSmokers);
        smoke.SmokersSearch();
    }

    public void setUp(ArrayList<Integer[]> nonSmokers) {
        Integer[] temp = new Integer[2];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                temp[0] = r;
                temp[1] = c;
                if (nonSmokers.contains(temp)) {
                    grid[r][c] = null;
                } else {
                    grid[r][c] = new Node(r, c);
                }
            }
        }
        for (Integer[] nonSmoker : nonSmokers) {
            grid[nonSmoker[1]][nonSmoker[0]].isValid = false;
        }
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

    public void SmokersSearch() {
        ArrayList<Node> path = getPath();
        ArrayList<Node> next_path;
        ArrayList<Integer[]> avoid = nonSmokers;
        int[] indivudalDistance = new int[nonSmokersCoordinates.size() / 2];
        for (int a = 0; a < indivudalDistance.length; a++) {
            indivudalDistance[a] = Integer.MAX_VALUE;
        }
        int theMin = Integer.MAX_VALUE;
        if (path != null) {
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (path.contains(grid[r][c])) {
                        System.out.print("+ ");
                        int counter = 0;
                        for (int z = 0; z < nonSmokersCoordinates.size(); z += 2) {
                            int distance = Math.abs(nonSmokersCoordinates.get(z) - c) + Math.abs(nonSmokersCoordinates.get(z + 1) - r);
                            //   System.out.println("Distance is "+(distanceC+distanceD));
                            if (theMin > distance) {
                                theMin = (distance);
                            }
                            if (indivudalDistance[counter] > distance) {
                                indivudalDistance[counter] = distance;
                            }
                            counter++;
                        }
                        //  System.exit(0);
                    } else if (grid[r][c].isValid) {
                        System.out.print("o ");
                    } else {
                        System.out.print("x ");
                    }
                }
                System.out.println();
            }
            int total = 0;
            for (int b = 0; b < indivudalDistance.length; b++) {
                total += indivudalDistance[b];
            }
            System.out.println("min " + theMin + ", total " + total+"\n");
        }
    }

    public ArrayList<Node> getPath() {
        Node start = grid[0][0];
        Node finish = grid[grid.length - 1][grid[0].length - 1];
        ArrayList<Node> path;
        path = new ArrayList<>();
        Node current = start;
        path.add(current);

        while (current != finish) {
            Node n = current;
            double lowest_cost = 1000;
            Node next_node = current;
            for (Node node : n.neighbours) {
                if (node.isValid) {
                    double h = node.getH();
                    if (h < lowest_cost) {
                        lowest_cost = h;
                        next_node = node;
                    }
                }
            }
            current = next_node;
            path.add(current);
            if (path.size() > grid.length * grid[0].length) {
                return null;
            }
        }
        return path;
    }

    public void expandWalls(Node node) {
        for (Node n : grid[node.row][node.column].neighbours) {
            if (grid[n.row][n.column].isValid) {
                grid[n.row][n.column].isValid = false;
                Integer[] non = {n.row, n.column};
                nonSmokers.add(non);
            }
        }
    }

    public void undoExpandWalls(Node node) {
        for (Node n : grid[node.row][node.column].neighbours) {
            if (grid[n.row][n.column].isValid) {
                grid[n.row][n.column].isValid = true;
                Integer[] non = {n.row, n.column};
                nonSmokers.remove(non);
            }
        }
    }
}
