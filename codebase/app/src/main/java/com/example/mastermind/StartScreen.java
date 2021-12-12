package com.example.mastermind;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartScreen extends AppCompatActivity {

    private Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_screen);

        startGame = findViewById(R.id.StartGame);

        startGame.setOnClickListener(v -> {

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

            StartScreen.this.startActivity(mainActivity);

        });

    }



}
