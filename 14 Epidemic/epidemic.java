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
import java.io.*;

public class epidemic {

    // part 1, just used for set up the input array to work on, and the arguements given.
    public static int[][] inputArray;
    public static String inputline;
    public static int mode = 0;
    public static boolean foundSolution = false;
    public static List < String > tempfilelist = new ArrayList < > ();
    public static int columncount = 0;
    public static int rowcount = 0;

    // part 2

    /*
    So the genes array is what is going to be tested on, represending the entire grid filled up randomly with 1's and 0's.
    I treat the 1's as sick and 0 as non sick non immune, when there is a clash (input says this cell is immune but gene says this cell is sick),
    it will return 0 for fitness which I think does hurt the GA overall. 
    
    It would probably be better to have the genelength the size of the grid minus the immune and 
    just calculate the genes which are relevant, plugging them into the fitness method and adding the immunes then, but I thought i might have problems 
    as I convert alot between 1d and 2d arrays in this.
    
    The immune hashset is just to detect the clashes and return the fitness of 0, A 10% mutation seemed to work well, higher mutations would give me incorrect values too often,
    I basically just kept rerunning the program to see if the results where fluctuating or stable.
    
    Next population was used for the next generation after the tournament and cross over has taken place.
    
    In order to reduce the fluctuation of results, I had to use things like biggestfit and a 20 forloop to obtain the best result over multiple solutions as I will explain more below.
    
    fitnessArrayTorny is used to keep track of the fitness score when genes compiete with one another, which will be used to calculate the fittest of each tornament held.
    fitnessArray holds the fitness results of the entire population used to find which genes perform the best overall 
    */
    public static int geneLength = columncount * rowcount;
    public static int populationSize = 50;
    public static int[][] currentPopulation = new int[populationSize][geneLength];
    public static int[] fitnessArray = new int[populationSize];
    public static int[][] nextPopulation = new int[populationSize][geneLength];
    public static HashSet < Integer > immune = new HashSet < Integer > ();
    public static int[] genes = new int[geneLength];
    public static double mutation = 0.1;
    public static int tournamentSize = 30;
    public static int biggestfit = 0;
    public static int[] fitnessArrayTorny = new int[tournamentSize];




    public static void main(String[] args) throws FileNotFoundException, IOException {
        //Sets up input array and finds out which mode the user wants.
        mode = Integer.parseInt(args[0]);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String myString = scanner.nextLine();
            if (columncount == 0) {
                rowcount = myString.length();
            }

            tempfilelist.add(myString);
            if (myString.length() == 0) {
                setupArray();
            } else {
                columncount++;
            }
        }

        scanner.close();

        setupArray();
    }


    // Used to setup the input array, converting the charaters into numbers.
    public static void setupArray() {

        inputArray = new int[columncount][rowcount];

        for (int x = 0; x < columncount; x++) {
            for (int y = 0; y < rowcount; y++) {
                if (tempfilelist.get(x).charAt(y) == 'S') {
                    inputArray[x][y] = 1;
                }
                if (tempfilelist.get(x).charAt(y) == 'I') {
                    inputArray[x][y] = 2;
                }
                if (tempfilelist.get(x).charAt(y) == '.') {
                    inputArray[x][y] = 0;
                }
            }
        }
        if (mode == 1) {
            finalgeneration(inputArray);
            printer();
            System.out.println();
        }
        if (mode == 2) {
            foundSolution = false;
            part2();
            System.out.println();
        }
        // clear everything for subsequent inputs.
        columncount = 0;
        rowcount = 0;
        tempfilelist.clear();
    }



    public static void part2() {
        // The hashset that contains the position of the immune cells is cleared for each input 
        immune.clear();
        // creates the genes which will be the full grid size full of 0's, Also creates the 2d arrays 50 size population (no particular reason for the 50)
        geneLength = rowcount * columncount;
        genes = new int[geneLength];
        nextPopulation = new int[populationSize][geneLength];
        currentPopulation = new int[populationSize][geneLength];

        //This will add the immunes to the hashset so any gene array which has a 1 in these positions will be punished later.
        int immuneindex = 0;
        for (int a = 0; a < columncount; a++) {
            for (int b = 0; b < rowcount; b++) {
                if (inputArray[a][b] == 2) {
                    immune.add(immuneindex);
                }
                immuneindex++;
            }
        }
        /* I would get solutions which where incorrect, as my fitness calculator gives good scores even if the solution does not actually solve the problem,
        which means I have to check if the solution is actually valid which can be easily checked by my solution for part 1 of the assignment.
        
        smallest= just keeps track of the smallest number of sick people possible, so if i get 15 14 15 16 it will remember 14
        bestarray= just keeps track of the array with the best solution ie 14 sick
        testarray= used to check if the solution is actually valid, so only genes that leave everything sick is accepted.
*/
        boolean valid = false;
        int[] finalresult;
        int[][] testarray = new int[columncount][rowcount];
        int[] bestarray = new int[columncount * rowcount];
        int smallest = rowcount * columncount;


        /*
        I used a loop of 20 here to prevent fluctuation and reduce the change of incorrect results. So it will find 20 valid solutions and pick the best one.
        */
        for (int n = 0; n < 20; n++) {
            valid = false;
            finalresult = getFittestGenes();
            testarray = new int[columncount][rowcount];
            int sickcounter = 0;

            // keep checking until a solution actually infects everything
            while (valid == false) {
                /*
                Generate population will just create a new population so current population will 50 sets of genes all 1's and 0's randomly
                */
                generatePopulation();

                /*
                Will calculate fitness, which i will explain in the method
                */
                calcFitness();
                int prev = biggestfit;
                int repeat = 0;
                /*
                This will go through many generations until no change can be seen in fitness 30 times in a row.
                */
                while (repeat < 30) {
                    calcFitness();
                    if (prev == biggestfit) {
                        repeat++;
                    } else {
                        repeat = 0;
                    }
                    prev = biggestfit;
                }

                /* get the fittest genes after no subsequent generations improve the genes.
            It will have no immunes (2) in it, So they will be added to the gene array so it can be tested correctly
            to check if the solution is valid
            
            printcounter = used to get the correct index for the array
            sick counter= used later to check if this current array is better (has less mandatory sick) Lowest sick count will be kept
            */
                finalresult = getFittestGenes();
                int printcounter = 0;
                sickcounter = 0;
                for (int i = 0; i < columncount; i++) {
                    for (int j = 0; j < rowcount; j++) {
                        if (immune.contains(printcounter)) {
                            finalresult[printcounter] = 2;
                        }

                        if (finalresult[printcounter] == 1 && !immune.contains(printcounter)) {
                            sickcounter++;
                        }
                        printcounter++;
                    }
                }



                // Convert the 1d gene array into a 2d array so my part 1 final generation can find out if the final generation gets completely infected
                testarray = new int[columncount][rowcount];
                int indexer = 0;
                for (int q = 0; q < columncount; q++) {
                    for (int w = 0; w < rowcount; w++) {
                        testarray[q][w] = finalresult[indexer];
                        indexer++;
                    }
                }


                valid = finalgeneration(testarray);
            }
            /*
            If its valid, check if its the best result, if so add it to get the best result possible
            */
            if (sickcounter < smallest) {
                smallest = sickcounter;
                bestarray = finalresult;
            }
        }

        /*
        Best array will be put into the 2d array input Array, this is so my printer() will print out to terminal correctly.
        */
        int indexprint = 0;
        for (int e = 0; e < columncount; e++) {
            for (int f = 0; f < rowcount; f++) {
                inputArray[e][f] = bestarray[indexprint];
                indexprint++;
            }
        }
        System.out.println(smallest);
        printer();
    }


    // Used in both part 1 and part 2. Part 2 its used to check if the input completely infects the grid, returns false if it doesnt
    public static boolean finalgeneration(int[][] epiArray) {

        boolean change = true;
        boolean noNonImmune = true;

        // If there was a difference detected after one scan of the grid and new infections where found
        while (change) {
            change = false;
            for (int x = 0; x < columncount; x++) {
                for (int y = 0; y < rowcount; y++) {
                    int nearinfection = 0;

                    if (epiArray[x][y] == 0) {
                        noNonImmune = false;
                        if (x > 0) {
                            if (epiArray[x - 1][y] == 1) {
                                nearinfection++;
                                // infection above
                            }
                        }
                        if (x < columncount - 1) {
                            if (epiArray[x + 1][y] == 1) {
                                nearinfection++;
                                // infection below
                            }
                        }
                        if (y > 0) {
                            if (epiArray[x][y - 1] == 1) {
                                nearinfection++;
                                //   "Infected left"
                            }
                        }
                        if (y < rowcount - 1) {
                            if (epiArray[x][y + 1] == 1) {
                                nearinfection++;
                                //     "Infected right"
                            }
                        }


                        if (nearinfection > 1) {
                            change = true;
                            epiArray[x][y] = 1;
                            noNonImmune = true;

                        }
                    }
                }
            }
        }

        return noNonImmune;
    }

    public static void printer() {
        // prints the output grid in the correct format
        for (int x = 0; x < columncount; x++) {
            for (int y = 0; y < rowcount; y++) {
                if (inputArray[x][y] == 1) {
                    System.out.print("S");
                }
                if (inputArray[x][y] == 2) {
                    System.out.print("I");
                }
                if (inputArray[x][y] == 0) {
                    System.out.print(".");
                }

            }
            System.out.println("");


        }

    }

    //Creates a new population from array of individuals, so it will be 2d array size 50 filled with 0/1 genes
    public static void generatePopulation() {
        for (int i = 0; i < populationSize; i++) {
            generateIndividual();
            for (int j = 0; j < geneLength; j++) {
                currentPopulation[i][j] = genes[j];
            }
        }

    }

    // Hold the fitness values in an array, this will find the biggest fitness and give the index of that value in the array
    public static int getFittestIndex() {
        int index = 0;
        for (int i = 0; i < fitnessArray.length; i++) {
            if (fitnessArray[i] > fitnessArray[index]) {
                index = i;
            }
        }
        return index;
    }

    // Uses the fitness index to obtain the gene with the highest fitness in the 2d population array, will then return the fittest gene.
    public static int[] getFittestGenes() {
        int[] result = new int[geneLength];

        for (int i = 0; i < geneLength; i++) {
            result[i] = currentPopulation[getFittestIndex()][i];
        }
        biggestfit = fitnessArray[getFittestIndex()];
        return result;

    }



    // Generates array of genes 0's and 1's randomly. 
    public static void generateIndividual() {
        for (int i = 0; i < geneLength; i++) {
            byte gene = (byte) Math.round(Math.random());
            genes[i] = gene;
        }
    }

    public static void calcFitness() {

        // Will go through the entire population (50)
        for (int i = 0; i < populationSize; i++) {

            // Will take each row(gene) of the 2d array (population) and turn them each into their own 2d arrays (the grid) to be used for fitness calculator (epicArray)
            int[] row = new int[geneLength];
            for (int j = 0; j < geneLength; j++) {
                row[j] = currentPopulation[i][j];
            }
            int counterfit = 0;
            int[][] epicArray = new int[columncount][rowcount];
            for (int c = 0; c < columncount; c++) {
                for (int r = 0; r < rowcount; r++) {
                    epicArray[c][r] = row[counterfit];
                    counterfit++;
                }
            }
            // Calculates the fitness for each gene in the population, then adds it to the fitness array to be used to detect the best performing.
            fitnessArray[i] = fitnessCalculator(epicArray);
        }
        adapt();
    }

    /* Used to calculate the performance of each gene. 
    First check if there is a clash between a gene which has a sick cell (1) which should actually be immune, if this is the case, give it a fitness of 0
    to remove it from the gene pool.
    
    The rest of this is the same as my part1 solution (final generation), but it will count the total sick and subtract it from the initial sick it detected at the start.
    */
    public static int fitnessCalculator(int[][] inputArray) {
        int sickCounter = 0;
        int arraycounter = 0;
        for (int b = 0; b < columncount; b++) {
            for (int c = 0; c < rowcount; c++) {
                if (immune.contains(arraycounter)) {
                    //   return 0;
                    if (inputArray[b][c] == 1) {
                        return 0;

                    }
                    inputArray[b][c] = 2;
                }
                if (inputArray[b][c] == 1) {
                    sickCounter++;
                }
                arraycounter++;
            }
        }
        boolean change = true;
        while (change) {
            change = false;
            for (int x = 0; x < columncount; x++) {
                for (int y = 0; y < rowcount; y++) {
                    int nearinfection = 0;

                    if (inputArray[x][y] == 0) {
                        if (x > 0) {
                            if (inputArray[x - 1][y] == 1) {
                                nearinfection++;
                                // infection above
                            }
                        }
                        if (x < columncount - 1) {
                            if (inputArray[x + 1][y] == 1) {
                                nearinfection++;
                                // infection below
                            }
                        }
                        if (y > 0) {
                            if (inputArray[x][y - 1] == 1) {
                                nearinfection++;
                                //   "Infected left"
                            }
                        }
                        if (y < rowcount - 1) {
                            if (inputArray[x][y + 1] == 1) {
                                nearinfection++;
                                //     "Infected right"
                            }
                        }


                        if (nearinfection > 1) {
                            change = true;
                            inputArray[x][y] = 1;

                        }
                    }
                }
            }
        }
        int infectioncount = 0;
        for (int a = 0; a < columncount; a++) {
            for (int b = 0; b < rowcount; b++) {
                if (inputArray[a][b] == 1) {
                    infectioncount++;
                }

            }
        }
        return infectioncount - sickCounter;
    }




    /*
    Uses tournament selection to create the new population. The gene that is the fittest in the current total population will be guarenteed to exist in the next generation.
    Two samples of the gene pool is selected, which will compete within each other and the fittest of the sample will be the parent genes which ultimately combine their genes to a child
    ie 000000 and 111111 parent genes will give the child 001101 genes as I am using a 50% crossover and for each gene in the child, a 10% chance for a specific gene to
    have a 50% chance to swap.
    
    Ultimately the new population will be filled with these children and the 1 elite gene.
    */
    public static void adapt() {
        int[] topGene = getFittestGenes();

        // Save fittest gene for next generation
        for (int i = 0; i < geneLength; i++) {
            nextPopulation[0][i] = topGene[i];
        }
        for (int j = 1; j < populationSize; j++) {
            // two fittest parents from a sample of the population
            int[] firstindividual = torny();
            int[] secondindividual = torny();
            // child created from half of each parents genes
            int[] child = crossover(firstindividual, secondindividual);
            // For each gene of the child, there is a 10% chance a new gene will be generated (50% chance it will change)
            for (int m = 0; m < child.length; m++) {
                if (Math.random() <= mutation) {
                    byte gene = (byte) Math.round(Math.random());
                    child[m] = gene;
                }
            }
            for (int v = 0; v < geneLength; v++) {
                nextPopulation[j][v] = child[v];
            }

        }
        // replace the old gene pool with the new gene pool.
        currentPopulation = nextPopulation;
    }

    public static int[] crossover(int[] indiv1, int[] indiv2) {
        int[] child = new int[geneLength];
        // Takes a gene from either of the two parents to give to the child
        for (int i = 0; i < geneLength; i++) {
            if (Math.random() <= 0.5) {
                child[i] = indiv1[i];
            } else {
                child[i] = indiv2[i];
            }
        }


        return child;
    }


    /*
        This will generate samples of the genepool to compete against each other, so out of a population of 50, 30 random genes will be taken to compete.
        Just like before with calcfitness, those arrays will be taken out of the 2d array population and converted into the grid pattern in epicarray to calculate the fitness
        which will be held in fitnessArrayTorny
        It will then find the highest performing in the tournament population and make it one of the two parents. 
        */
    public static int[] torny() {
        int[][] tournamentPop = new int[tournamentSize][geneLength];
        int[] fittestTorny = new int[geneLength];
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int)(Math.random() * populationSize);
            for (int a = 0; a < geneLength; a++) {
                tournamentPop[i][a] = currentPopulation[randomId][a];
            }

        }

        for (int t = 0; t < tournamentSize; t++) {
            int[][] epicArray = new int[columncount][rowcount];
            int count = 0;
            for (int c = 0; c < columncount; c++) {
                for (int r = 0; r < rowcount; r++) {
                    epicArray[c][r] = tournamentPop[t][count];
                    count++;
                }
            }

            fitnessArrayTorny[t] = fitnessCalculator(epicArray);
        }

        int index = 0;
        for (int s = 0; s < fitnessArrayTorny.length; s++) {
            if (fitnessArrayTorny[index] < fitnessArrayTorny[s]) {
                index = s;
            }
        }


        for (int z = 0; z < geneLength; z++) {
            fittestTorny[z] = tournamentPop[index][z];
        }
        return fittestTorny;
    }

}