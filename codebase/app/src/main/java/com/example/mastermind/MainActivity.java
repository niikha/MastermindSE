package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instatiate all table rows

        // Create a ConstraintLayout in which to add the ImageView
         tableLayout = findViewById(R.id.gameboard);

        // Add the ImageView to the layout and set the layout as the content view.
        drawPin(1, 1, 1);
        drawPin(1, 2, 2);
        drawPin(1, 3, 3);
        drawPin(1, 4, 4);
        drawPin(1, 5, 5);


        //constraintLayout.addView(gridLayout);
        //setContentView(constraintLayout);

    }

    public void drawPin(int row, int column, int id){
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
}