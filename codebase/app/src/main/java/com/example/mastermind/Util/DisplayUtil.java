package com.example.mastermind.Util;

import android.graphics.Point;
import android.view.WindowManager;

public class DisplayUtil {
    /**
     * Calculates the coordinate values for a single dimension
     * @param totalLength
     *  Total dimension length in pixels
     * @param count
     *  Number of coordinates to be returned
     * @return
     *  A array of length 'count', containing the values
     */
    public static int[] calculateGridCoordinates(int totalLength, int count){

        int[] coords = new int[count];
        int margin = totalLength / count;
        int current = margin / 2; //Coordinates should be the middle of the grid cells -> offset by half

        for (int i = 0; i < count; i++){
            coords[i] = current;
            current += margin;
        }

        return coords;
    }
}
