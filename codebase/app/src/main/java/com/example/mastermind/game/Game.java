package com.example.mastermind.game;

import android.util.Pair;

import java.util.Arrays;

public class Game {

    private int currentStep;
    private int codeLength;
    private int[] code;

    public Game(int rounds, int codeLength){
        this.codeLength = codeLength;
        this.code = new int[codeLength];
        currentStep = 0;
    }

    public void createRandomCode(){

    }

    /**
     *
     * @return First integer representing the number of black pins (colour and position correct), second integer representing the number of white pins (only colour correct)
     */
    public Pair<Integer, Integer> validateGuess(int[] guess) throws Exception {

        //check if guess is same length as code

        if(guess.length != codeLength){
            throw new Exception(
                    "Length of guess (" + guess.length + ") does not match code length (" + codeLength + ")"
            );
        }

        //get number of black and white pins to be returned

        int black = 0;
        int white = 0;

        for(int i = 0; i < codeLength; i++){
            if (guess[i] == code [i])
                black++;
            else if (Arrays.asList(code).contains(guess[i]))
                white++;
        }

        return new Pair<>(black, white);
    }
}
