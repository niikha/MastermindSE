package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mastermind.Util.ArrayUtil;
import com.example.mastermind.Util.DisplayUtil;
import com.example.mastermind.game.Game;
import com.example.mastermind.game.GuessValidationResult;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    private final int rowCount = 11;
    private final int columnCount = 5;

    private int[] colCoordinates;
    private int[] rowCoordinates;
    private int[][] gameField; //row first, column second
    private int currentSelectionIndex;
    private int currentRow;
    private boolean gameStopped; //'true' if game is won or lost, 'false' if game is in progress
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

        //setup for field and start new game
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
        for (int i = 1; i < rowCount - 2; i++){
            for (int j = 0; j < columnCount - 1; j++){
                drawPin(i, j, gameField[i][j], false);
            }
        }
    }

    /**
     * Instantiates the game field with empty pins
     */
    private void generateGameField(){
        this.gameField = new int[rowCount - 2][columnCount - 1];
        clearGameField();
    }

    /**
     * Sets every pin in the playing field to empty (pin-id = 0)
     */
    private void clearGameField(){
        for (int i = 1; i < rowCount - 2; i++){
            for (int j = 0; j < columnCount - 1; j++){
                this.gameField[i][j] = 0;
            }
        }
    }

    /**
     * Draws the question mark pins at the top, the selection pins and button at the bottom and empty textures in the evaluation column
     */
    private void drawFunctionalField(){

        //for each column
        for (int i = 0; i < columnCount - 1; i++){

            //draw question mark pin
            drawPin(0, i, 9, false);

            //draw selection pins (if the game is stopped, the 'win' or 'loss' message is displayed instead)
            if (!gameStopped){
                drawPin(rowCount - 2, i, i + 1, true);
                drawPin(rowCount - 1, i,  i + 5, true);
            }
        }

        //set last column (where guess results will be shown) to empty
        for (int i = 1; i < rowCount - 2; i++){
            drawPin(i, columnCount - 1, -1, false);
        }

        //Draw reset if game is stopped) or guess button to bottom right corner
        if (gameStopped)
            drawResetButton();
        else
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
    public void drawPin(int row, int column, int id, boolean isSelectionButton){

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

    private void drawResultView(boolean isGameWon){

        //get ImageView
        ImageView view = new ImageView(this);
        if (isGameWon)
            view.setImageResource(R.drawable.result_won_temp);
        else
            view.setImageResource(R.drawable.result_lost_temp);

        //set size
        int size = (int)(relativeTextureSize() * 3.2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size * 2, size);

        //set x and y coordinates
        int x = (colCoordinates[((columnCount - 1 )/ 2)] + colCoordinates[((columnCount - 1 )/ 2) - 1]) / 2 - size;
        int y = (rowCoordinates[rowCount-2] + rowCoordinates[rowCount-1]) / 2 - size / 2;
        view.setX(x);
        view.setY(y);

        relativeLayout.addView(view, params);
    }

    private void drawResetButton(){
        //set image source
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.btn_retry_temp);

        //set size
        int size = (int)(relativeTextureSize() * 1.4);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size * 2);

        //set x and y coordinates
        int x = colCoordinates[columnCount-1] - size / 2;
        int y = (rowCoordinates[rowCount-2] + rowCoordinates[rowCount-1]) / 2 - size;
        view.setX(x);
        view.setY(y);

        //set on-click listener
        view.setOnClickListener(v -> startNewGame());

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
        this.colCoordinates = DisplayUtil.calculateGridCoordinates(sizeX, columnCount);

        //calculate Y coordinates (rows)
        this.rowCoordinates = DisplayUtil.calculateGridCoordinates(sizeY, rowCount);
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
        if (!gameStopped){
            this.gameField[currentRow][currentSelectionIndex] = id;
            currentSelectionIndex++;
            if(currentSelectionIndex == 4){
                currentSelectionIndex = 0;
            }
            refreshGrid();
        }
    }

    private void submitGuess(){
        if (!gameStopped){
            int[] selection = ArrayUtil.arrayFromField(this.gameField, currentRow, ArrayUtil.ArrayDimensions.ROW);
            if (!ArrayUtil.ArrayContainsValue(selection, 0)){
                try{
                    GuessValidationResult result = this.game.validateGuess(selection);
                    if(!checkGameStatus()){
                        currentRow = rowCount - 3 - game.getCurrentRound();
                        displayGuessResult(result);
                    }
                    currentSelectionIndex = 0;
                }
                catch (Exception e) {
                    //T0D0: Fehlermeldung anzeigen
                }
            }
        }
    }

    /**
     * Checks if game is won or lost
     * @return
     *  Returns true if game is won or lost, otherwise false
     */
    private boolean checkGameStatus(){
        if (this.game.isGameWon()){
            gameEnd(true);
            return true;
        }
        if (this.game.isGameLost()){
            gameEnd(false);
            return true;
        }
        return false;
    }

    private void startNewGame(){
        gameStopped = false;
        this.game = new Game(8, 4, 8, false);
        this.game.createRandomCode();
        clearGameField();
        refreshGrid();
        currentRow = rowCount - 3;
        currentSelectionIndex = 0;
    }

    private void displayGuessResult(GuessValidationResult result){
        //T0D0
    }

    private void gameEnd(boolean isGameWon){
        gameStopped = true;
        refreshGrid();
        drawResultView(isGameWon);
    }
}