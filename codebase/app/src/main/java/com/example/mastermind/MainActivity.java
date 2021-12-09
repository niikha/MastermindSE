package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    private final int columnCount = 5;
    private final int rowCount = 11;

    private int[] colCoordinates;
    private int[] rowCoordinates;
    private int[][] field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Get Gameboard Layout
         relativeLayout = findViewById(R.id.gameboard);

         //setup for field
        generateCoordinates();
        generateField();
        drawField();
    }

    /**
     * Clears the entire field and draws it again
     */
    public void refreshField(){
        relativeLayout.removeAllViews();
        drawField();
    }

    /**
     * Draws the entire field
     */
    public void drawField(){
        for (int i = 0; i < columnCount; i++){
            for (int j = 0; j < rowCount; j++){
                drawPin(i, j, field[i][j]);
            }
        }
    }

    /**
     * Instantiates the game field with empty pins
     */
    private void generateField(){
        this.field = new int[columnCount][rowCount];
        clearField();
    }

    /**
     * Sets every pin in the field to empty (pin-id = 0)
     */
    private void clearField(){
        for (int i = 0; i < columnCount; i++){
            for (int j = 0; j < rowCount; j++){
                this.field[i][j] = 10;
            }
        }
    }

    /**
     * Draws a specified pin to a specified position
     * @param column
     *  Column in which to draw the pin
     * @param row
     *  Row in which to draw the pin
     * @param id
     *  The pin's colour ID
     */
    public void drawPin(int column, int row, int id){
        ImageView view = getPinFromID(id);
        int x = colCoordinates[column];
        int y = rowCoordinates[row];
        view.setX(x);
        view.setY(y);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
        params.gravity=0;
        params.weight=0;
        params.setMarginEnd(0);
        params.setMargins(0,0,0,0);
        params.setMarginStart(0);
        relativeLayout.addView(view, params);
    }

    /**
     * Generates an ImageView with a pin of the specified colour ID
     * @param id
     *  Colour ID for the pin (0 for 'empty')
     * @return
     *  An ImageView with a pin in the specified colour
     */
    public ImageView getPinFromID(int id){
        ImageView view = new ImageView(this);
        switch (id){
            case 0:
                view.setImageResource(R.drawable.pin_empty);
                break;
            case 1:
                view.setImageResource(R.drawable.pin_black);
                break;
            case 2:
                view.setImageResource(R.drawable.pin_blue);
                break;
            case 3:
                view.setImageResource(R.drawable.pin_green);
                break;
            case 4:
                view.setImageResource(R.drawable.pin_cyan);
                break;
            case 5:
                view.setImageResource(R.drawable.pin_purple);
                break;
            case 6:
                view.setImageResource(R.drawable.pin_red);
                break;
            case 7:
                view.setImageResource(R.drawable.pin_white);
                break;
            case 8:
                view.setImageResource(R.drawable.pin_yellow);
                break;
            default:
                view.setImageResource(R.drawable.invalid_texture);
                break;
        }
        view.setAdjustViewBounds(true);
        return view;
    }

    /**
     * Instantiates and calculates the coordinates for the game grid
     */
    private void generateCoordinates(){

        //get display dimensions
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int sizeX = size.x;
        int sizeY = size.y;

        //calculate X coordinates (columns)
        this.colCoordinates = calculateGridCoordinates(sizeX, columnCount);

        //calculate Y coordinates (rows)
        this.rowCoordinates = calculateGridCoordinates(sizeY, rowCount);
    }

    /**
     * Calculates the coordinate values for a single dimension
     * @param totalLength
     *  Total dimension length in pixels
     * @param count
     *  Number of coordinates to be returned
     * @return
     *  A array of length 'count', containing the values
     */
    private int[] calculateGridCoordinates(int totalLength, int count){

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