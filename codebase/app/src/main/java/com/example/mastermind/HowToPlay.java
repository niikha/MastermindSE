package com.example.mastermind;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HowToPlay extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_howtoplay_screen);

        textView = findViewById(R.id.textView);

        textView.setText(Html.fromHtml(
                "<b>Spielregeln:</b><br />Es stehen 8 verschieden-farbige Kugeln zur Auswahl." +
                " Beim Spielstart wird ein zufälliger Code aus 4 unterschiedlicher Kugeln erzeugt, welcher erraten werden muss." +
                " Hierzu hat man 8 Züge zur Verfügung. In jedem Zug müssen alle 4 Stellen des Codes belegt sein." +
                " Nach der Auswertung eines Zuges erscheint in dieser Reihe eine schwarze und eine weiße Zahl." +
                " Die schwarze Zahl symbolisiert die Anzahl der Volltreffer und die weiße Zahl die Anzahl der Treffer." +
                "<br /><b><u>Volltreffer:</u></b> X Stellen des Codes wurden erraten." +
                "<br /><b><u>Treffer:</u></b> X Kugeln des Codes wurden erraten, jedoch wurden auf der falschen Position platziert." +
                " <br /><br /><b>Schweres Spiel:</b><br />Die Besonderheit bei diesem Spielmodus ist, dass der zufällige Code alle Farbkombinationen besitzen kann." +
                " Es müssen nicht mehr 4 unterschiedliche Kugeln im Code sein. Können aber! :)"));
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}