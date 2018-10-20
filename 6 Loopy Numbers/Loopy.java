/**
 * Our program uses hashsets as they are optimized for
 * quick searches where order is irrelevant.
 * Happy values and detected loops are added to the ban
 * list to prevent repetition.
 *
 *
 */
import java.util.*;
public class LoopyV4 {

    /**
     * @param args the command line arguments
     */
    public static HashSet < Integer > banned = new HashSet < Integer > ();
    public static HashSet < Integer > currentSearch = new HashSet < Integer > ();
    public static HashSet < Integer > solutionsHash = new HashSet < Integer > ();
    public static int maxloop = 0;
    public static int loopnumber = 0;
    public static int maxnumber = 9000000;
    public static List < Integer > solutions = new ArrayList < > ();
    public static int counter=1;

    public static void main(String[] args) {
        System.out.println("Searching for loopy numbers less than " + maxnumber + ", please wait");
        int percent = 0;
        for (int i = 1; i <= maxnumber; i++) {
            loopSearcher(i);
            if (i % (maxnumber / 10) == 0) {
                percent += 10;
                System.out.println(percent + "% complete");
            }
        }
       // System.out.println("\n" + solutionsHash.size() + " Loopy numbers have been found!");
        System.out.println("\nNumber of loops found: "+solutions.size());
        System.out.println("The largest loop found is " + maxloop + " numbers long\n");
        resultoptions();
    }

    public static void resultoptions() {
        System.out.println("Search Complete, please select any of the following options for the results\n");
        Scanner scan = new Scanner(System.in);
        while (true) {
            int options = 0;
            System.out.println("Press 1 to view all the loopy numbers found");
            System.out.println("Press 2 to view the corresponding loops found");
            System.out.println("Press 3 to check if a number is loopy and less than " + maxnumber);
            System.out.println("Press 4 to end the program");
            try {
                options = scan.nextInt();
            } catch (Exception e) {
                scan.next();
            }
            if (options == 1) {
                System.out.println("List of the loopy numbers found");
                int i = 0;
                ArrayList < Integer > sort = new ArrayList < Integer > (solutionsHash);
                Collections.sort(sort);
                for (int a = 0; a < sort.size(); a++) {
                    System.out.print(sort.get(a) + "\t");
                    i++;
                    if (i % 10 == 0) {
                        System.out.println();
                    }
                   
                }
                 System.out.println("\n" + solutionsHash.size() + " Loopy numbers have been found!");
            }

            if (options == 2) {
                System.out.println("Loopy numbers found and their corresponding loops");
                counter=1;
                for (int i = 0; i < solutions.size(); i++) {
                    printLoop(solutions.get(i));
                }
                System.out.println("\nNumber of loops found: "+solutions.size());
            }
            if (options == 3) {
                while (true) {
                    System.out.println("\nEnter number you want to check or 'quit' to return to main menu");
                    String input = scan.next();
                    if (input.equals("quit")) {
                        break;
                    } else {
                        try {
                            int inputvalue = Integer.parseInt(input);
                            if (solutionsHash.contains(inputvalue)) {
                                System.out.println(inputvalue + " is a loopy number ");
                            } else {
                                System.out.println(inputvalue + " does not exist in the list ");
                            }
                        } catch (Exception e) {
                            scan.next();
                        }
                    }

                }
            }
            if (options == 4) {
                System.exit(0);
            }
            System.out.println("\n");

        }
    }


    public static void loopSearcher(int i) {
        int currentresult = i;
        currentSearch.add(currentresult);
        while (currentresult != 1 && currentresult < maxnumber && !banned.contains(currentresult)) {
            int nextresult = factorcalc(currentresult);
            if (nextresult == currentresult) {
                solutions.add(nextresult);
                 loopNumberCounter(currentresult);
                    loopnumber++;
                break;
            } else {
                currentresult = nextresult;
                if (currentSearch.contains(currentresult)) {
                    solutions.add(currentresult);
                    loopNumberCounter(currentresult);
                    loopnumber++;
                    banned.add(currentresult);
                    break;
                }
                currentSearch.add(currentresult);
            }
        }
        banned.addAll(currentSearch);
        currentSearch.clear();

    }
    public static void loopNumberCounter(int x) {
        int counter = 2;
        solutionsHash.add(x);
        int currentresult = factorcalc(x);
        solutionsHash.add(currentresult);
        while (factorcalc(currentresult) != x) {
            currentresult = factorcalc(currentresult);
            solutionsHash.add(currentresult);
            counter++;
        }
        if (counter > maxloop) {
            maxloop = counter;
        }
    }



    public static void printLoop(int x) {
        System.out.print("Loop"+counter+":\t"+x + "\t");
        counter++;
        int currentresult = factorcalc(x);
        System.out.print(currentresult + "\t");
        while (factorcalc(currentresult) != x) {
            currentresult = factorcalc(currentresult);
            System.out.print(currentresult + "\t");
        }
        System.out.println();
    }



    public static int factorcalc(int num) {
        int sumfactor = 0;
        for (int i = 1; i <= Math.sqrt(num); i += num % 2 == 0 ? 1 : 2) {
            if (num % i == 0) {
                sumfactor += i;
                int div = num / i;
                if (i != div && div != num) {
                    sumfactor += div;
                }

            }
        }

        return sumfactor;
    }
}