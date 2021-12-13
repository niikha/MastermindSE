package com.example.mastermind;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mastermind.game.Game;

public class StartScreen extends AppCompatActivity {

    private Button startGame;
    private Button gameRules;
    private Button exitGame;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_screen);

        startGame = findViewById(R.id.StartGame);
        gameRules = findViewById(R.id.Spielregeln);
        exitGame = findViewById(R.id.Exit);

        startGame.setOnClickListener(v -> {

            Intent mainActivity = new Intent(getApplicationContext(), DifficultyScreen.class);

            StartScreen.this.startActivity(mainActivity);

        });

        exitGame.setOnClickListener(v -> {
            finish();
        });

        gameRules.setOnClickListener(v -> {

                }
                );

    }



}
