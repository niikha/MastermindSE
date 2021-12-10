package com.example.mastermind;

public class Util {

    /**
     * Checks if the array contains an integer of the specified value
     * @param array Array to be searched
     * @param value Value to be searched for
     * @return True if array contains said value, otherwise false
     */
    public static boolean ArrayContainsValue(int[] array, int value) {
        for (int i : array) {
            if (i == value)
                return true;
        }
        return false;
    }

    public static int[] fillArray(int length, int value){
        int[] toReturn = new int[length];
        for (int i=0; i<length;i++)
            toReturn[i] = value;
        return toReturn;
    }
}
