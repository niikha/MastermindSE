package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mastermind.game.Game;
import com.example.mastermind.game.GuessValidationResult;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    private final int columnCount = 5;
    private final int rowCount = 11;

    private int[] colCoordinates;
    private int[] rowCoordinates;
    private int[][] gameField;
    private int[] selection = new int[]{-1, -1, -1, -1};
    private int currentSelectionIndex;
    private int currentRow; //später weg, nur zum testen
    private Game game;

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
        currentRow = 8;
        currentSelectionIndex=0; //remove later or call at every new guess
        generateCoordinates();
        generateGameField();
        startNewGame();
        drawGameField();
        drawFunctionalField();
    }

    /**
     * Clears the entire grid and draws game and functional field again
     */
    public void refreshGrid(){
        relativeLayout.removeAllViews();
        drawGameField();
        drawFunctionalField();
    }

    /**
     * Draws the entire game field
     */
    public void drawGameField(){
        for (int i = 0; i < columnCount - 1; i++){
            for (int j = 1; j < rowCount - 2; j++){
                drawPin(i, j, gameField[i][j], false);
            }
        }
    }

    /**
     * Instantiates the game field with empty pins
     */
    private void generateGameField(){
        this.gameField = new int[columnCount - 1][rowCount - 2];
        clearGameField();
    }

    /**
     * Sets every pin in the playing field to empty (pin-id = 0)
     */
    private void clearGameField(){
        for (int i = 1; i < rowCount - 2; i++){
            for (int j = 0; j < columnCount - 1; j++){
                this.gameField[j][i] = 0;
            }
        }
    }

    /**
     * Draws the question mark pins at the top, the selection pins and button at the bottom and empty textures in the evaluation column
     */
    private void drawFunctionalField(){
        for (int i = 0; i < columnCount - 1; i++){
            drawPin(i, 0, 9, false);//this.gameField[i][0] = 9;
            drawPin(i, rowCount - 2, i + 1, true);//this.gameField[i][rowCount-2] = i+1;
            drawPin(i, rowCount - 1, i + 5, true);//this.gameField[i][rowCount-1] = i + columnCount;
        }
        for (int i = 1; i < rowCount - 2; i++){
            drawPin(columnCount - 1, i, -1, false);//this.gameField[columnCount-1][i] = -1;
        }
        drawGuessButton();
    }

    private void drawGuessButton(){

        //set image source
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.btn_guess_temp);

        //set size
        int size = (int)(relativeTextureSize() * 1.4);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size * 2);

        //set x and y coordinates
        int x = colCoordinates[columnCount-1] - size / 2;
        int y = (rowCoordinates[rowCount-2] + rowCoordinates[rowCount-1]) / 2 - size;
        view.setX(x);
        view.setY(y);

        //set on-click listener
        view.setOnClickListener(v -> submitGuess());

        relativeLayout.addView(view, params);
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
    public void drawPin(int column, int row, int id, boolean isSelectionButton){

        //get ImageView
        ImageView view = getPinFromID(id);

        //set size
        int size = (int)(relativeTextureSize() * 1.3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);

        //set x and y coordinates for pin (Center of texture should be on point (default would be top left corner) -> Offset by half of size in -x and -y direction)
        int offset = size / 2;
        int x = colCoordinates[column] - offset;
        int y = rowCoordinates[row] - offset;
        view.setX(x);
        view.setY(y);

        //if the pin is a selection button, set the on-click listener
        if(isSelectionButton){
            view.setOnClickListener(v -> selectPin(id));
        }

        //Add pin to layout
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
            case -1:
                //return empty view
                break;
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
            case 9:
                view.setImageResource(R.drawable.pin_unknown);
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

    /**
     * Relative texture size is equivalent to 100 pixels on a display with 1920 pixels in height
     */
    private int relativeTextureSize(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size.y * 100 / 1920;
    }

    private void selectPin(int id){
        this.selection[currentSelectionIndex] = id;
        this.gameField[currentSelectionIndex][currentRow] = id;
        currentSelectionIndex++;
        if(currentSelectionIndex == 4){
            currentSelectionIndex = 0;
        }
        refreshGrid();
    }

    private void submitGuess(){
        try{
            GuessValidationResult result = this.game.validateGuess(selection);
            if(!checkGameStatus()){
                currentRow = rowCount - 3 - game.getCurrentRound();
            }
            currentSelectionIndex = 0;
        }
        catch (Exception e) {
            //T0D0: Fehlermeldung anzeigen
        }
    }

    /**
     * Checks if game is won or lost
     * @return
     *  Returns true if game is won or lost, otherwise false
     */
    private boolean checkGameStatus(){
        if (this.game.isGameWon()){
            //do something
            return true;
        }
        if (this.game.isGameLost()){
            //do something
            return true;
        }
        return false;
    }

    private void startNewGame(){
        this.game = new Game(8, 4, 8, false);
        this.game.createRandomCode();
        clearGameField();
        refreshGrid();
    }

    private void displayResult(GuessValidationResult result){
        //T0D0
    }
}