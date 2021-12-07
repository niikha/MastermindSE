package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    private final int columnCount = 4;
    private final int rowCount = 8;

    private int[] colCoorinates; //x = 4
    private int[] rowCoordinates;//y = 8
    private int[][] field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Gameboard Layout
         linearLayout = findViewById(R.id.gameboard);

         //setup for field
        generateCoordinates();
        generateField();
        drawField();
        
        // Add the ImageView to the layout and set the layout as the content view.
        drawPin(1, 1, 1);
        drawPin(1, 2, 2);
        drawPin(1, 3, 3);
        drawPin(1, 4, 4);
        drawPin(1, 5, 5);


        //constraintLayout.addView(gridLayout);
        //setContentView(constraintLayout);

    }

    public void refreshField(){
        linearLayout.removeAllViews();
        drawField();
    }

    public void drawField(){
        for (int i = 0; i < columnCount; i++){
            for (int j = 0; j < rowCount; j++){
                drawPin(i, j, field[i][j]);
            }
        }
    }

    public void drawPin(int column, int row, int id){
        ImageView view = getPinFromID(id);
    }

    public ImageView getPinFromID(int id){
        ImageView view = new ImageView(this);
        switch (id){
            case 0:
                view.setImageResource(R.drawable.pin_empty);
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
        }
        view.setAdjustViewBounds(true);
        view.setX(10);
        view.setY(10);
        return view;
    }

    //Grid coordinates in dpi
    private void generateCoordinates(){
        this.colCoorinates = new int[]{};
        this.rowCoordinates = new int[]{};
    }

    private void generateField(){
        this.field = new int[columnCount][rowCount];
        clearField();
    }


    private void clearField(){
        for (int i = 0; i < columnCount; i++){
            for (int j = 0; j < rowCount; j++){
                field[i][j] = 0;
            }
        }
    }
}