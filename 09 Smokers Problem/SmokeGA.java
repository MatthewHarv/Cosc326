/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
import java.util.*;
import java.util.stream.*;

public class SmokeGA {

    public static int columncount;
    public static int rowcount;
    public static int geneLength;
    public static int populationSize = 200;
    public static int[][] currentPopulation = new int[populationSize][geneLength];
    public static int[] fitnessArray = new int[populationSize];
    public static int[][] nextPopulation = new int[populationSize][geneLength];
    public static int[] genes = new int[geneLength];
    public static double mutation = 0.035;
    public static int tournamentSize = 20;
    public static int biggestfit = 0;
    public static int[] fitnessArrayTorny = new int[tournamentSize];
    public static int minResult = 0;
    public static int totalResult = 0;
    public static int numberofNonSmokers;
    public static ArrayList<Integer> nonSmokerCoordinates = new ArrayList<Integer>();

    public static int[][] grid;

    //  2 1
    // 3 0
    //System.out.println(Arrays.toString(bestArray));
    //  System.out.println(Arrays.deepToString(nextPopulation));
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int countinput = 0;
        while (scanner.hasNext()) {
            String myString = scanner.nextLine();
            if (myString.length() == 0) {
                countinput = 0;
                ga();
            } else {
                if (countinput == 0) {
                    String arr[] = myString.split(" ", 2);
                    String firstWord = arr[0];
                    String secondWord = arr[1];
                    columncount = Integer.parseInt(firstWord);
                    rowcount = Integer.parseInt(secondWord);
                    countinput++;
                    geneLength = ((rowcount + columncount) * 8);
			
                    grid = new int[columncount][rowcount];
                    grid[0][0] = 2;
                } else {
                    numberofNonSmokers++;
                    String arr[] = myString.split(" ", 2);
                    int firstValue = Integer.parseInt(arr[0]);
                    int secondValue = Integer.parseInt(arr[1]);
                    grid[secondValue][firstValue] = 1;
                }
            }
        }
        ga();
    }

    public static void ga() {
        //    System.out.println("Arraylist contains: " + nonSmokerCoordinates.toString());  
        int[][] testinggrid = new int[columncount][rowcount];

        for (int i = 0; i < grid.length; i++) {
            testinggrid[i] = grid[i].clone();
        }

        for (int c = 0; c < columncount; c++) {
            for (int r = 0; r < rowcount; r++) {
                if (testinggrid[c][r] == 1) {
                    //     System.out.println(c+" "+r);
                    nonSmokerCoordinates.add(c);
                    nonSmokerCoordinates.add(r);
                }
            }
        }
        nextPopulation = new int[populationSize][geneLength];
        currentPopulation = new int[populationSize][geneLength];
        // System.out.println("This is gene length "+geneLength);
        genes = new int[geneLength];
      //  double defaultMutation = mutation;
        int exprev = 0;
        int externalRepeater = 0;
        while (externalRepeater < 20) {

            int num = 0;
            generatePopulation();
            fitnessCalc();
            int prev = biggestfit;
            int internalRepeater = 0;
            while (internalRepeater < 3) {
                fitnessCalc();
                if (prev == biggestfit) {
                    internalRepeater++;
                } else {
                    internalRepeater = 0;
                }
                
                prev = biggestfit;
            }

            if (biggestfit > num) {
                num = biggestfit;
            }
            if (exprev >= totalResult) {
      //          mutation += 0.15;
                externalRepeater++;
            } else {
           //     mutation = defaultMutation;
                externalRepeater = 0;
                exprev = totalResult;
            }
       //     System.out.println(totalResult);
        }
    //    mutation = defaultMutation;

        System.out.println("min " + minResult + ", total " + totalResult);
        reset();
    }

    public static void reset() {
        nonSmokerCoordinates.clear();
        minResult = 0;
        totalResult = 0;
        numberofNonSmokers = 0;
    }

    public static int moveGeneCalc(int[] moveArray) {
        int left = moveArray[0];
        int right = moveArray[1];
        int up = moveArray[2];
        int down = moveArray[3];
        if (left == 1 && right == 1) {
            left = 0;
            right = 0;
        }
        if (up == 1 && down == 1) {
            up = 0;
            down = 0;
        }

        if (up + right + down + left == 1) {
            if (left == 1) {
                return 1;
            }
            if (right == 1) {
                return 2;
            }
            if (up == 1) {
                return 3;
            }
            if (down == 1) {
                return 4;
            }
        }
        return 0;
    }

    public static void generatePopulation() {
        for (int i = 0; i < populationSize; i++) {
            generateIndividual();
            System.arraycopy(genes, 0, currentPopulation[i], 0, geneLength);
        }

    }

    public static void generateIndividual() {
        Random r = new Random();
        for (int i = 0; i < geneLength; i++) {
            float chance = r.nextFloat();

            if (chance <= 0.5f) {
                genes[i] = 1;
            } else {
                genes[i] = 0;
            }

        }
    }

    public static int fitnessCalculator(int[] input) {
        int fitness = 0;
        int[][] testinggrid = new int[columncount][rowcount];

        for (int i = 0; i < grid.length; i++) {
            testinggrid[i] = grid[i].clone();
        }
        int[] mindistance = new int[numberofNonSmokers];
        for (int g = 0; g < mindistance.length; g++) {
            mindistance[g] = rowcount * columncount;
        }

        int smokerColumn = 0;
        int smokerRow = 0;

        //  System.out.println(Arrays.toString(mindistance));
        for (int i = 0; i < input.length; i += 4) {
            int distcountindex = 0;
            for (int v = 0; v < nonSmokerCoordinates.size(); v += 2) {
                int a = Math.abs(nonSmokerCoordinates.get(v) - smokerColumn);
                int b = Math.abs(nonSmokerCoordinates.get(v + 1) - smokerRow);
                int distance = a + b;
                if (mindistance[distcountindex] > distance) {
                    mindistance[distcountindex] = distance;
                }
                distcountindex++;
            }

            int[] foursGene = {
                input[i],
                input[i + 1],
                input[i + 2],
                input[i + 3]
            };
            int movement = moveGeneCalc(foursGene);
            boolean moved = false;
            switch (movement) {
                case 1:
                    //left
                    if (smokerRow > 0 && testinggrid[smokerColumn][smokerRow - 1] != 1) {
                        testinggrid[smokerColumn][smokerRow - 1] = 2;
                        testinggrid[smokerColumn][smokerRow] = 0;
                        smokerRow--;
                        moved = true;

                    }
                    break;
                case 2:
                    //right
                    if (smokerRow < rowcount - 1 && testinggrid[smokerColumn][smokerRow + 1] != 1) {
                        testinggrid[smokerColumn][smokerRow + 1] = 2;
                        testinggrid[smokerColumn][smokerRow] = 0;
                        smokerRow++;
                        moved = true;
                    }
                    break;
                case 3:
                    //up
                    if (smokerColumn > 0 && testinggrid[smokerColumn - 1][smokerRow] != 1) {
                        testinggrid[smokerColumn - 1][smokerRow] = 2;
                        testinggrid[smokerColumn][smokerRow] = 0;
                        smokerColumn--;
                        moved = true;
                    }
                    break;
                case 4:
                    //down
                    if (smokerColumn < columncount - 1 && testinggrid[smokerColumn + 1][smokerRow] != 1) {
                        testinggrid[smokerColumn + 1][smokerRow] = 2;
                        testinggrid[smokerColumn][smokerRow] = 0;
                        smokerColumn++;
                        moved = true;
                    }
                    break;
                default:
                    break;
            }

            if (moved == true) {
                //      fitness += 10;
            } else {
                 //    fitness -=15;
            }
            if (smokerColumn == columncount - 1 && smokerRow == rowcount - 1) {
                Arrays.sort(mindistance);
            //
           fitness += mindistance[0];
                fitness += (IntStream.of(mindistance).sum()*2);
                if (minResult < mindistance[0]) {
                    minResult = mindistance[0];
                    totalResult = IntStream.of(mindistance).sum();
                } else if (minResult == mindistance[0] && totalResult < IntStream.of(mindistance).sum()) {
                    totalResult = IntStream.of(mindistance).sum();
                }
                return fitness;
            }
        

        }
        fitness+=smokerColumn;
        fitness+=smokerRow;
    //    Arrays.sort(mindistance);
     //   fitness += mindistance[0] * 2;
    //    fitness += IntStream.of(mindistance).sum();
        //  System.exit(0);
        return fitness;
    }

    public static void fitnessCalc() {
        for (int i = 0; i < populationSize; i++) {
            int[] currentArray = currentPopulation[i];
            fitnessArray[i] = fitnessCalculator(currentArray);
        }
        adapt();
    }

    public static void adapt() {
        int[] topGene = getFittestGenes();
        System.arraycopy(topGene, 0, nextPopulation[0], 0, geneLength);
        for (int j = 1; j < populationSize; j++) {
            int[] firstindividual = torny();
            int[] secondindividual = torny();
            int[] child = crossover(firstindividual, secondindividual);
            for (int m = 0; m < child.length; m++) {
                if (Math.random() <= mutation) {
                    byte gene = (byte) Math.round(Math.random());
                    child[m] = gene;
                }
            }
            System.arraycopy(child, 0, nextPopulation[j], 0, geneLength);

        }
        currentPopulation = nextPopulation;
    }

    public static int[] crossover(int[] indiv1, int[] indiv2) {
        int[] child = new int[geneLength];
        for (int i = 0; i < geneLength; i++) {
            if (Math.random() <= 0.5) {
                child[i] = indiv1[i];
            } else {
                child[i] = indiv2[i];
            }
        }
        return child;
    }

    public static int[] torny() {
        int[][] tournamentPop = new int[tournamentSize][geneLength];
        int[] fittestTorny = new int[geneLength];
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * populationSize);
            System.arraycopy(currentPopulation[randomId], 0, tournamentPop[i], 0, geneLength);
        }
        for (int t = 0; t < tournamentSize; t++) {
            int[] epicArray = new int[geneLength];
            for (int i = 0; i < geneLength; i++) {
                int toepic = tournamentPop[t][i];
                epicArray[i] = toepic;
            }
            fitnessArrayTorny[t] = fitnessCalculator(epicArray);
        }
        int index = 0;
        for (int s = 0; s < fitnessArrayTorny.length; s++) {
            if (fitnessArrayTorny[index] < fitnessArrayTorny[s]) {
                index = s;
            }
        }
        System.arraycopy(tournamentPop[index], 0, fittestTorny, 0, geneLength);
        return fittestTorny;
    }

    public static int[] getFittestGenes() {
        int[] result = new int[geneLength];

        System.arraycopy(currentPopulation[getFittestIndex()], 0, result, 0, geneLength);
        biggestfit = fitnessArray[getFittestIndex()];
        return result;

    }

    public static int getFittestIndex() {
        int index = 0;
        for (int i = 0; i < fitnessArray.length; i++) {
            if (fitnessArray[i] > fitnessArray[index]) {
                index = i;
            }
        }
        return index;
    }

}
