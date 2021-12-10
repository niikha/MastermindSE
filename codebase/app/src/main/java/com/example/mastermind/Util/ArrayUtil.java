package com.example.mastermind.Util;

public class ArrayUtil {

    public enum ArrayDimensions{
        ROW,
        COLUMN
    }

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

    /**
     * Gets a specific row or column from a two-dimensional array
     * @param field
     *  The two-dimensional array
     * @param index
     *  The index of the one-dimensional array in the field
     * @param toGet
     *  Whether to get a row or a column
     * @return
     */
    public static int[] arrayFromField(int[][] field, int index, ArrayDimensions toGet){
        int[] toReturn;
        switch (toGet){
            case ROW:
                toReturn = new int[field[index].length];
                for (int i = 0; i < field[index].length; i++)
                    toReturn[i] = field[index][i];
                break;
            case COLUMN:
                toReturn = new int[field.length];
                for (int i = 0; i< field.length; i++)
                    toReturn[i] = field[i][index];
                break;
            default:
                toReturn = new int[0];
                break;
        }
        return toReturn;
    }
}
