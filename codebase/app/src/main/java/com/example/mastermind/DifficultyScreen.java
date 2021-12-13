package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DifficultyScreen extends AppCompatActivity {

    private Button normalGame;
    private Button difficultGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_screen);

        normalGame = findViewById(R.id.normal_Game);
        difficultGame = findViewById(R.id.difficult_Game);

        normalGame.setOnClickListener(v -> {
            Intent game = new Intent(getApplicationContext(), MainActivity.class);
            game.putExtra("difficulty", "normal");
            DifficultyScreen.this.startActivity(game);
        });

        difficultGame.setOnClickListener(v -> {
            Intent game = new Intent(getApplicationContext(), MainActivity.class);
            game.putExtra("difficulty", "difficult");
            DifficultyScreen.this.startActivity(game);
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}