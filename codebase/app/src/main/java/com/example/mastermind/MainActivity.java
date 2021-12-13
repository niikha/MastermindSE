package com.example.mastermind;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
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
    private boolean doubleValues;

    private int[] colCoordinates;
    private int[] rowCoordinates;
    private int[][] gameField; //row first, column second
    private int currentSelectionIndex;  //position in the current row where the next pin will be placed
    private int currentRow; //index of the current row in the game field
    private boolean gameStopped; //'true' if game is won or lost, 'false' if game is in progress
    private Game game;
    private GuessValidationResult[] results; //The results of past and current guesses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //get difficulty
        String difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty == "difficult")
            doubleValues = true;
        else
            doubleValues = false;

        //Get Gameboard Layout
         relativeLayout = findViewById(R.id.gameboard);

        //setup for field and start new game
        generateCoordinates();
        generateGameField();
        startNewGame();
    }

    /**
     * Clears the entire grid and draws game and functional field again
     */
    public void refreshGrid(){
        relativeLayout.removeAllViews();
        drawBackground();
        drawGameField();
        drawFunctionalField();
        displayGuessResults();
    }

    private void drawBackground(){
        relativeLayout.setBackground(getResources().getDrawable(R.drawable.background));
    }

    /**
     * Draws the entire game field
     */
    private void drawGameField(){
        for (int i = 1; i < rowCount - 2; i++){
            for (int j = 0; j < columnCount - 1; j++){
                drawPin(i, j, gameField[i][j]);
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

        //draw the slightly darker background behind the selection buttons
        drawButtonBackground();

        //for each column
        for (int i = 0; i < columnCount - 1; i++){

            //draw question mark pin
            drawPin(0, i, 9);

            //draw selection pins (if the game is stopped, the 'win' or 'loss' message is displayed instead)
            if (!gameStopped){
                drawPin(rowCount - 2, i, i + 1, true);
                drawPin(rowCount - 1, i,  i + 5, true);
            }
        }

        //set last column (where guess results will be shown) to empty
        for (int i = 1; i < rowCount - 2; i++){
            drawPin(i, columnCount - 1, -1);
        }

        drawExitButton();

        //Draw reset (if game is stopped) or guess button to bottom right corner
        if (gameStopped)
            drawResetButton();
        else
            drawGuessButton();
    }

    /**
     * Draws a darker background behind the selection buttons
     */
    private void drawButtonBackground(){

        //get imageView
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.transparent_box);

        //set size
        int size = (int)(relativeTextureSize() * 3.5);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(size*5), size);

        //set x and y
        int x = (colCoordinates[((columnCount - 1 )/ 2)] + colCoordinates[((columnCount - 1 )/ 2) - 1]) / 2 - (int)(size*1.8);
        int y = (rowCoordinates[rowCount-1] + rowCoordinates[rowCount-2]) / 2 - (int)(size*0.5);
        view.setX(x);
        view.setY(y);

        relativeLayout.addView(view, params);
    }

    /**
     * Draws a specified non-button pin to a specified position
     * @param column
     *  Column in which to draw the pin
     * @param row
     *  Row in which to draw the pin
     * @param id
     *  The pin's colour ID
     */
    private void drawPin(int row, int column, int id){
        drawPin(row, column, id, false);
    }

    /**
     * Draws a specified pin to a specified position
     * @param column
     *  Column in which to draw the pin
     * @param row
     *  Row in which to draw the pin
     * @param id
     *  The pin's colour ID
     * @param isSelectionButton
     *  Whether the pin is one of the buttons used to select pins
     */
    private void drawPin(int row, int column, int id, boolean isSelectionButton){

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
     * Draws the 'you win' or 'you loose' message
     * @param isGameWon
     *  True if game was won, false if game was lost
     */
    private void drawResultView(boolean isGameWon){

        //get ImageView
        ImageView view = new ImageView(this);
        if (isGameWon)
            view.setImageResource(R.drawable.result_won_temp);
        else
            view.setImageResource(R.drawable.result_lost_temp);

        //set size
        int size = (int)(relativeTextureSize() * 3.5);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size * 2, size);

        //set x and y coordinates
        int x = (colCoordinates[((columnCount - 1 )/ 2)] + colCoordinates[((columnCount - 1 )/ 2) - 1]) / 2 - size;
        int y = (rowCoordinates[rowCount-2] + rowCoordinates[rowCount-1]) / 2 - size / 2;
        view.setX(x);
        view.setY(y);

        relativeLayout.addView(view, params);
    }

    /**
     * Draws the button for submitting the guess to the bottom right corner
     */
    private void drawGuessButton(){
        drawBottomButton(R.drawable.btn_guess_temp, v -> submitGuess());
    }

    /**
     * Draws the button for resetting the game to the bottom right corner
     */
    private void drawResetButton(){
        drawBottomButton(R.drawable.btn_retry_temp, v -> startNewGame());
    }

    /**
     * Draws a button in the bottom right corner
     * @param resourceId
     *  Drawable resource for the button image
     * @param listener
     *  On-Click listener for the button
     */
    private void drawBottomButton(@DrawableRes int resourceId, View.OnClickListener listener){
        //set image source
        ImageView view = new ImageView(this);
        view.setImageResource(resourceId);

        //set size
        int size = (int)(relativeTextureSize() * 1.4);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size * 2);

        //set x and y coordinates
        int x = colCoordinates[columnCount-1] - size / 2;
        int y = (rowCoordinates[rowCount-2] + rowCoordinates[rowCount-1]) / 2 - size;
        view.setX(x);
        view.setY(y);

        //set on-click listener
        view.setOnClickListener(listener);

        relativeLayout.addView(view, params);
    }

    private void drawExitButton(){

        //get ImageView
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.btn_exit_temp);

        //set size
        int size = (int)(relativeTextureSize() * 1.3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);

        //set x and y
        int x = colCoordinates[columnCount - 1 ]  - size / 2;
        int y = rowCoordinates[0] - size / 2;
        view.setX(x);
        view.setY(y);

        //set on-click listener
        view.setOnClickListener(v -> exitGame());

        relativeLayout.addView(view, params);
    }

    /**
     * Generates an ImageView with a pin of the specified colour ID
     * @param id
     *  Colour ID for the pin (0 for 'empty')
     * @return
     *  An ImageView with a pin in the specified colour
     */
    private ImageView getPinFromID(int id){
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

    /**
     * On-Click listener for pin selection buttons; adds the pin to the field
     * @param id
     *  The colour ID of the pin
     */
    private void selectPin(int id){

        //Button should only work if game is running
        if (!gameStopped){
            //draw the pin to the current position
            this.gameField[currentRow][currentSelectionIndex] = id;

            //move on to the next position (roll over if currently at last position)
            currentSelectionIndex++;
            if(currentSelectionIndex == columnCount - 1){
                currentSelectionIndex = 0;
            }
            refreshGrid();
        }
    }

    /**
     * On-Click listener for submit button; submits the current guess to validation and checks the result
     */
    private void submitGuess(){

        //Button should only work if game is running
        if (!gameStopped){

            //get the pins in the current row and check if none is empty (ID 0)
            int[] selection = ArrayUtil.arrayFromField(this.gameField, currentRow, ArrayUtil.ArrayDimensions.ROW);
            if (!ArrayUtil.ArrayContainsValue(selection, 0)){

                //submit the guess and draw the result
                try{
                    GuessValidationResult result = this.game.validateGuess(selection);
                    drawGuessResult(currentRow, result);
                    results[rowCount - 3 - currentRow] = result;

                    //check the result
                    if(!checkHasGameEnded()){
                        //if the game hasn't ended, add result to results and move to the next row
                        currentRow = rowCount - 3 - game.getCurrentRound();
                    }
                    currentSelectionIndex = 0;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Checks if game is won or lost, ends game if true
     * @return
     *  Returns 'true' if game is won or lost, 'false' if not
     */
    private boolean checkHasGameEnded(){
        if (this.game.isGameWon()){
            endGame(true);
            return true;
        }
        if (this.game.isGameLost()){
            endGame(false);
            return true;
        }
        return false;
    }

    /**
     * Creates a new game, resets grid and selection
     */
    private void startNewGame(){
        this.game = new Game(8, 4, 8, doubleValues);
        this.game.createRandomCode();
        gameStopped = false;
        this.results = new GuessValidationResult[rowCount-2];
        clearGameField();
        refreshGrid();
        currentRow = rowCount - 3;
        currentSelectionIndex = 0;
    }

    /**
     * Displays results for all guesses
     */
    private void displayGuessResults(){
        for (int i = 0; i < this.results.length; i++){
            GuessValidationResult result = this.results[i];
            if (result != null)
                drawGuessResult(rowCount - 3 - i, result);
        }
    }

    /**
     * Draws a guess result in a row
     * @param row
     *  Row in which to draw the result
     * @param result
     *  The corresponding result
     */
    private void drawGuessResult(int row, GuessValidationResult result){
        ImageView viewBlack = new ImageView(this);
        ImageView viewWhite = new ImageView(this);

        //get texture for black number
        switch (result.GetBlack()){
            case 0:
                viewBlack.setImageResource(R.drawable.b_null_113x140);
                break;
            case 1:
                viewBlack.setImageResource(R.drawable.b_one_113x140);
                break;
            case 2:
                viewBlack.setImageResource(R.drawable.b_two_113x140);
                break;
            case 3:
                viewBlack.setImageResource(R.drawable.b_three_113x140);
                break;
            case 4:
                viewBlack.setImageResource(R.drawable.b_four_113x140);
                break;
            default:
                viewBlack.setImageResource(R.drawable.invalid_texture);
                break;
        }

        //get texture for white number
        switch (result.GetWhite()){
            case 0:
                viewWhite.setImageResource(R.drawable.w_null_113x140);
                break;
            case 1:
                viewWhite.setImageResource(R.drawable.w_one_113x140);
                break;
            case 2:
                viewWhite.setImageResource(R.drawable.w_two_113x140);
                break;
            case 3:
                viewWhite.setImageResource(R.drawable.w_three_113x140);
                break;
            case 4:
                viewWhite.setImageResource(R.drawable.w_four_113x140);
                break;
            default:
                viewWhite.setImageResource(R.drawable.invalid_texture);
                break;
        }

        //set size
        int size = (int)(relativeTextureSize() * 0.7);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size * 2);

        //get x and y for both views
        int xwhite = colCoordinates[columnCount-1];
        int xblack = xwhite - (int)(1.5 * size);
        int y = rowCoordinates[row] - size;
        viewBlack.setX(xblack);
        viewBlack.setY(y);
        viewWhite.setX(xwhite);
        viewWhite.setY(y);

        relativeLayout.addView(viewBlack, params);
        relativeLayout.addView(viewWhite, params);
    }

    /**
     * Displays the code in place of the question mark pins
     */
    private void displayCode(){
        int[] code = this.game.getCode();
        for (int i = 0; i < code.length; i++){
            drawPin(0, i, code[i]);
        }
    }

    /**
     * Stops the game and draws the result view
     * @param isGameWon
     *  Indicates if the game was won or lost
     */
    private void endGame(boolean isGameWon){
        gameStopped = true;
        refreshGrid();
        displayCode();
        drawResultView(isGameWon);
    }

    /**
     * On-Click listener for exit button; exit game back to main menu screen
     */
    private void exitGame(){
        finish();
    }
}