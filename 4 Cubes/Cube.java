/*

 */

import java.util.*;

/**
 *
 * @author PC
 *
 *
 *
 */
public class Cube {

    public static ArrayList<Integer> cubeList = new ArrayList<Integer>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Integer size = 24;
        Integer counter = 0;
        for (Integer j = 0; j <= size; j++) {
            BitSet bitset = new BitSet(size);
            bitset.set(0, j);
            Integer[] generatedArray = new Integer[size];
            while (true) {
                for (Integer i = 0; i < size; i++) {
                    Integer generatedbit = Integer.parseInt((bitset.get(i) ? "1" : "0"));
                    generatedArray[i] = generatedbit;
                }
                if (checkValidCube(generatedArray)) {
                   if (cubeEngine(generatedArray)) {
                        counter++;
                       cubeList.addAll(Arrays.asList(generatedArray));
              //         System.out.println(generatedArray[0]+" "+generatedArray[1]+" "+generatedArray[2]+" "+generatedArray[3]+" "+generatedArray[4]+" "+generatedArray[5]+" "+generatedArray[6]+" "+generatedArray[7]);
                    }
                }
                Integer prevclear = bitset.previousClearBit(size - 1);
                Integer setbit = bitset.previousSetBit(prevclear);
                if (setbit == -1) {
                    break;
                }
                Integer bitin = setbit + (size + 1 - prevclear);
                bitset.clear(setbit);
                bitset.set(setbit + 1, bitin);
                bitset.clear(bitin, size);
            }
        }
        System.out.println(counter + " Possible combinations");
    }

    public static boolean checkValidCube(Integer[] input) {
        //                                 0                4            8              12               16            20
        //   int[] testArray=new int[]{TOP[1,1,1,1],BOTTOM[0,0,0,0],LEFT[1,1,0,0],RIGHT[1,1,0,0],FORWARD[1,1,0,0],BACK[1,1,0,0]};     
        return input[0] == input[8] && input[0] == input[21] && input[1] == input[20] && input[1] == input[13] && input[3] == input[12] && input[3] == input[17] && input[2] == input[16] && input[2] == input[9] && input[4] == input[11] && input[4] == input[18] && input[5] == input[19] && input[5] == input[14] && input[6] == input[23] && input[6] == input[10] && input[7] == input[15] && input[7] == input[22];
    }

    public static boolean cubeEngine(Integer[] input) {
        boolean result = true;
        Integer[] testArray1 = input.clone();
        Integer[] testArray2 = input.clone();

        //rotate clockwise
        Integer[] rotateClock1 = rotateZAxis(testArray1).clone();
        Integer[] rotateClock2 = rotateZAxis(rotateClock1).clone();
        Integer[] rotateClock3 = rotateZAxis(rotateClock2).clone();
          
        //rotate Yaxis+clockwise
        Integer[] rotate1 = rotateYAxis(testArray1).clone();
        Integer[] rotateClock4 = rotateZAxis(rotate1).clone();
        Integer[] rotateClock5 = rotateZAxis(rotateClock4).clone();
        Integer[] rotateClock6 = rotateZAxis(rotateClock5).clone();
        Integer[] rotate2 = rotateYAxis(rotate1).clone();
        Integer[] rotateClock7 = rotateZAxis(rotate2).clone();
        Integer[] rotateClock8 = rotateZAxis(rotateClock7).clone();
        Integer[] rotateClock9 = rotateZAxis(rotateClock8).clone();
        Integer[] rotate3 = rotateYAxis(rotate2).clone();
        Integer[] rotateClock10 = rotateZAxis(rotate3).clone();
        Integer[] rotateClock11 = rotateZAxis(rotateClock10).clone();
        Integer[] rotateClock12 = rotateZAxis(rotateClock11).clone();

        //rotate Xaxis+clockwise
        Integer[] rotate4 = rotateXAxis(testArray2).clone();
        Integer[] rotateClock16 = rotateZAxis(rotate4).clone();
        Integer[] rotateClock17 = rotateZAxis(rotateClock16).clone();
        Integer[] rotateClock18 = rotateZAxis(rotateClock17).clone();
        Integer[] rotate6 = rotateXAxis( rotateXAxis(rotate4).clone()).clone();
        Integer[] rotateClock22 = rotateZAxis(rotate6).clone();
        Integer[] rotateClock23 = rotateZAxis(rotateClock22).clone();
        Integer[] rotateClock24 = rotateZAxis(rotateClock23).clone();

        for (Integer j = 0; j < cubeList.size(); j += 24) {
            Integer thecubeList[] = new Integer[]{cubeList.get(j), cubeList.get(j + 1), cubeList.get(j + 2), cubeList.get(j + 3), cubeList.get(j + 4), cubeList.get(j + 5), cubeList.get(j + 6), cubeList.get(j + 7), cubeList.get(j + 8), cubeList.get(j + 9), cubeList.get(j + 10), cubeList.get(j + 11), cubeList.get(j + 12), cubeList.get(j + 13), cubeList.get(j + 14), cubeList.get(j + 15), cubeList.get(j + 16), cubeList.get(j + 17), cubeList.get(j + 18), cubeList.get(j + 19), cubeList.get(j + 20), cubeList.get(j + 21), cubeList.get(j + 22), cubeList.get(j + 23)};
            
            if (Arrays.equals(thecubeList, input)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotate1)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotate2)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotate3)) {
                result = false;
            }
           if (Arrays.equals(thecubeList, rotate4)) {
              result = false;
            }
            if (Arrays.equals(thecubeList, rotate6)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock1)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock2)) {
                result = false;
            }

            if (Arrays.equals(thecubeList, rotateClock3)) {
                result = false;
            }

            if (Arrays.equals(thecubeList, rotateClock4)) {
                result = false;
            }

            if (Arrays.equals(thecubeList, rotateClock5)) {
                result = false;
            }

            if (Arrays.equals(thecubeList, rotateClock6)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock7)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock8)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock9)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock10)) {
                result = false;
            }

            if (Arrays.equals(thecubeList, rotateClock11)) {
                result = false;
            }

            if (Arrays.equals(thecubeList, rotateClock12)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock16)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock17)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock18)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock22)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock23)) {
                result = false;
            }
            if (Arrays.equals(thecubeList, rotateClock24)) {
                result = false;
            }

        }

        return result;
    }

    public static Integer[] rotateYAxis(Integer[] input) {
        Integer[] returnArray = new Integer[24];
        returnArray[10] = input[0];
        returnArray[8] = input[1];
        returnArray[11] = input[2];
        returnArray[9] = input[3];
        returnArray[0] = input[13];
        returnArray[1] = input[15];
        returnArray[2] = input[12];
        returnArray[3] = input[14];
        returnArray[13] = input[7];
        returnArray[15] = input[6];
        returnArray[12] = input[5];
        returnArray[7] = input[10];
        returnArray[6] = input[8];
        returnArray[5] = input[11];
        returnArray[4] = input[9];
        returnArray[16] = input[17];
        returnArray[17] = input[19];
        returnArray[19] = input[18];
        returnArray[18] = input[16];
        returnArray[20] = input[22];
        returnArray[22] = input[23];
        returnArray[23] = input[21];
        returnArray[21] = input[20];
        returnArray[14] = input[4];
        return returnArray;
    }

    public static Integer[] rotateXAxis(Integer[] input) {
        Integer[] returnArray = new Integer[24];

        returnArray[23] = input[0];
        returnArray[22] = input[1];
        returnArray[20] = input[3];
        returnArray[21] = input[2];
        returnArray[0] = input[16];
        returnArray[1] = input[17];
        returnArray[2] = input[18];
        returnArray[3] = input[19];
        returnArray[16] = input[4];
        returnArray[17] = input[5];
        returnArray[18] = input[6];
        returnArray[19] = input[7];
        returnArray[4] = input[23];
        returnArray[5] = input[22];
        returnArray[6] = input[21];
        returnArray[7] = input[20];
        returnArray[10] = input[8];
        returnArray[8] = input[9];
        returnArray[9] = input[11];
        returnArray[11] = input[10];
        returnArray[15] = input[13];
        returnArray[13] = input[12];
        returnArray[12] = input[14];
        returnArray[14] = input[15];
        return returnArray;
    }

    public static Integer[] rotateZAxis(Integer[] input) {
        Integer[] returnArray = new Integer[24];
        returnArray[0] = input[2];
        returnArray[2] = input[3];
        returnArray[3] = input[1];
        returnArray[1] = input[0];
        returnArray[5] = input[7];
        returnArray[7] = input[6];
        returnArray[6] = input[4];
        returnArray[4] = input[5];
        returnArray[20] = input[8];
        returnArray[22] = input[10];
        returnArray[21] = input[9];
        returnArray[23] = input[11];
        returnArray[8] = input[16];
        returnArray[10] = input[18];
        returnArray[9] = input[17];
        returnArray[11] = input[19];
        returnArray[16] = input[12];
        returnArray[18] = input[14];
        returnArray[17] = input[13];
        returnArray[19] = input[15];
        returnArray[12] = input[20];
        returnArray[13] = input[21];
        returnArray[14] = input[22];
        returnArray[15] = input[23];
        return returnArray;
    }

}
