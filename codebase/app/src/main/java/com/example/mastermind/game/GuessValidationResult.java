package com.example.mastermind.game;

public class GuessValidationResult {

    private int black;
    private int white;

    public GuessValidationResult(int black, int white){
        this.black = black;
        this.white = white;
    }

    public int GetBlack(){
        return black;
    }

    public int GetWhite(){
        return white;
    }
}
