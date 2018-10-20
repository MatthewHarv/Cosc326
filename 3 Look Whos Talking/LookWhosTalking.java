
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author PC
 */
public class LookWhosTalking {

    public static String inputString = "";
    // 1==past     2= present   3=future
    public static int tense = 0;
    public static String theInput = "";

    public static void main(String[] args) throws UnsupportedEncodingException {
        try ( 
                Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String myString = scanner.nextLine();
                if (myString.length() > 0) {
                    translate((myString));
                }
            }
        }
    }

    public static void translate(String input) {
        tense = 0;
        theInput = input;
        inputString = input.toLowerCase();
        String verbfound = verbDetector(input);
        String pronoun = pronounConstructor(input);
        String tenseword = tenseReturn();
        thePrinter(tenseword, verbfound, pronoun);

    }

    public static String tenseReturn() {
        if (tense == 0) {
            if (containFullWord("are") || containFullWord("am")||containFullWord("is")) {
                tense = 2;
            } else if (containFullWord("where") || containFullWord("had")|| containFullWord("was")) {
                tense = 1;
            } else if (containFullWord("will")) {
                tense = 3;
            }
        }

        if (tense == 0) {
            if (containFullWord("go") || containFullWord("see")) {
                tense = 2;
            }

        }

        if (tense == 1) {
            return "I";
        }
        if (tense == 2) {
            return "Kei te";
        }
        if (tense == 3) {
            return "Ka";
        }
        return "";

    }

    public static String pronounConstructor(String input) {
        String result = "";
        if (input.contains("(") && input.contains(")")) {
            String bracketContent = input.substring(input.indexOf("(") + 1, input.indexOf(")"));
            String[] brackSplit = bracketContent.split(" ");
            for (char c : brackSplit[0].toCharArray()) {
                if (Character.isDigit(c)) {
                    int pronounNumber = Integer.parseInt(brackSplit[0]);
                    
                     
                    if (pronounNumber == 1) {
                        if (null == brackSplit[1]) {
                            return "";
                        } else {
                            switch (brackSplit[1]) {
                                case "incl":
                                        if (containFullWord("you")) {
                                        return "koe"; 
                                    }
                                  else {
                                        return "";
                                    }

                                case "excl":
                                    if (containFullWord("i") || containFullWord("me")) {
                                        return "au";
                                    }
                                   
                                    if (containFullWord("they") || containFullWord("them")) {
                                        return "ia";
                                    } else {
                                        return "";
                                    }
                                default:
                                    return "";
                            }
                        }
                    }

                    
                    if (pronounNumber == 2) {
                        if (null == brackSplit[1]) {
                            return "";
                        } else {
                            switch (brackSplit[1]) {
                                case "incl":
                                    if (containFullWord("we") || containFullWord("us")) {
                                        return "t" + "\u0101" + "ua";   //taua
                                    }
                                        if (containFullWord("you")) {
                                        return "k" + "\u014D" + "rua";  //korua  
                                    }
                                  else {
                                        return "";
                                    }

                                case "excl":
                                    if (containFullWord("we") || containFullWord("us")) {
                                        return "m" + "\u0101" + "ua";   //maua
                                    }
                                   
                                    if (containFullWord("they") || containFullWord("them")) {
                                        return "r" + "\u0101" + "ua";   //raua
                                    } else {
                                        return "";
                                    }
                                default:
                                    return "";
                            }
                        }

                    } else if (pronounNumber > 2) {
                        if (null == brackSplit[1]) {
                            return "";
                        } else {
                            switch (brackSplit[1]) {
                                case "incl":
                                    if (containFullWord("we") || containFullWord("us")) {
                                        return "t" + "\u0101" + "tou";   //tatou
                                    }
                                       if (containFullWord("you")) {
                                        return "koutou";
                                    }
                                    else {
                                        return "";
                                    }

                                case "excl":
                                    if (containFullWord("we") || containFullWord("us")) {
                                        return "m" + "\u0101" + "tou";  //matou
                                    }
                                    if (containFullWord("they") || containFullWord("them")) {
                                        return "r" + "\u0101" + "tou";  //ratou
                                    }
                                  else {
                                        return "";
                                    }
                                default:
                                    return "";
                            }
                        }
                    } else {
                        return "";
                    }

                }
            }

            //invalid number   
        } else if (containFullWord("i") || containFullWord("me")) {
            return "au";
        } else if (containFullWord("you")) {
            return "koe";
        } else if (containFullWord("he") || containFullWord("she") || containFullWord("him") || containFullWord("her")) {
            return "ia";
        }

        return result;
    }

    public static String verbDetector(String input) {
        if (containFullWord("go") || containFullWord("going")) {
            return "haere";
        }
        if (containFullWord("went")) {
            tense = 1;
            return "haere";
        }

        if (containFullWord("saw")) {
            tense = 1;
            return "kite";
        }
        if (containFullWord("wanted")) {
            tense = 1;
            return "hiahia";
        }
        if (containFullWord("called")) {
            tense = 1;
            return "karanga";
        }

        if (containFullWord("asked")) {
            tense = 1;
            return "p" + "\u0101" + "tai"; //patai
        }
        if (containFullWord("learnt")) {
            tense = 1;
            return "ako";
        }
        if (containFullWord("made")) {
            tense = 1;
            return "hanga";
        }
        

        if (containFullWord("make") || containFullWord("making")) {
            return "hanga";
        }
        if (containFullWord("see") || containFullWord("seeing")) {
            return "kite";
        }
        if (containFullWord("want") || containFullWord("wanting")) {
            return "hiahia";
        }

        if (containFullWord("call") || containFullWord("calling")) {
            return "karanga";
        }
        if (containFullWord("ask") || containFullWord("asking")) {
            return "p" + "\u0101" + "tai"; //patai
        }
        if (containFullWord("read") || containFullWord("reading")) {
            return "p" + "\u0101" + "nui";  //panui
        }
        if (containFullWord("learn") || containFullWord("learning")) {
            return "ako";
        }
        return "";
    }
    
    public static void thePrinter(String tenseword, String verbfound, String pronoun) {     
        if ("".equals(pronoun) && "".equals(verbfound) && tense == 0 || theInput.split("\\W+").length < 2) {
            System.out.println("invalid sentence");
        } else if ("".equals(verbfound)) {
            System.out.println("unknown verb: " + "\"" + theInput.substring(theInput.lastIndexOf(" ") + 1) + "\"");
        } else if ("".equals(pronoun)) {
              String arr[] = theInput.split(" ", 2);
            if (theInput.contains("(") && theInput.contains(")")) {
                String bracketContent = theInput.substring(theInput.indexOf("(") + 1, theInput.indexOf(")"));
                System.out.println("invalid pronoun reference " + "\"" + bracketContent + "\" with "+"\""+arr[0]+"\"");
            } else {
                System.out.println("invalid pronoun reference "+"\""+arr[0]+"\"");
            }  
        }
        else {
            if("".equals(tenseword)){
                System.out.println("Kei te" + " " + verbfound + " " + pronoun);
            } else{
            System.out.println(tenseword + " " + verbfound + " " + pronoun);
        }
        }
    }

    public static boolean containFullWord(String wordSearch) {
        return Pattern.compile("\\b" + wordSearch + "\\b").matcher(inputString).find();
    }
}
