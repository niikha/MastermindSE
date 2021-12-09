package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    private final int columnCount = 4;
    private final int rowCount = 8;

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

        // Add the ImageView to the layout and set the layout as the content view.


        //constraintLayout.addView(gridLayout);
        //setContentView(constraintLayout);

    }

    public void refreshField(){
        relativeLayout.removeAllViews();
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
        int x = colCoordinates[column]; //später aus tabelle
        int y = rowCoordinates[row];
        view.setX(x);
        view.setY(y);
        //view.setLeft(1000);
        //view.setTop(1000);
        //view.setMaxHeight(50);
        //view.setMaxWidth(50);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
        params.gravity=0;
        params.weight=0;
        params.setMarginEnd(0);
        params.setMargins(0,0,0,0);
        params.setMarginStart(0);
        relativeLayout.addView(view, params);
    }

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
        }
        view.setAdjustViewBounds(true);
        return view;
    }

    //Grid coordinates in dpi
    private void generateCoordinates(){
        this.colCoordinates = new int[]{108, 324, 540, 756, 972};
        this.rowCoordinates = new int[]{87, 261, 435, 609, 783, 957, 1131, 1305, 1479, 1653, 1827};
        //T0D0: evtl automatisch berechnen
    }

    private void generateField(){
        this.field = new int[columnCount][rowCount];
        clearField();
    }


    private void clearField(){
        for (int i = 0; i < columnCount; i++){
            for (int j = 0; j < rowCount; j++){
                this.field[i][j] = 0;
            }
        }
    }
}