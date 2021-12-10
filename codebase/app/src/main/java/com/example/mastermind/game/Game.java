package com.example.mastermind.game;

import com.example.mastermind.Util.ArrayUtil;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private int currentRound;
    private int codeLength;
    private int maxRounds;
    private int variants;
    private int[] code;
    private boolean doubleValuesAllowed;
    private boolean gameWon;
    private boolean gameLost;

    public int getCurrentRound() {
        return currentRound;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public int getVariants() {
        return variants;
    }

    public int[] getCode() {
        return code;
    }

    public boolean areDoubleValuesAllowed() {
        return doubleValuesAllowed;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public Game(int maxRounds, int codeLength, int variants, boolean doubleValuesAllowed) {
        this.codeLength = codeLength;
        this.code = new int[codeLength];
        this.maxRounds = maxRounds;
        this.variants = variants;
        currentRound = 0;
        this.doubleValuesAllowed = doubleValuesAllowed;
        gameWon = false;
        gameLost = false;
    }

    public void createRandomCode(){

        for (int i = 0; i<codeLength; i++){

            //generate random value
            int nextValue = ThreadLocalRandom.current().nextInt(variants);

            //if code already contains value, generate more values until value is unique
            if(!doubleValuesAllowed)
                while (Arrays.asList(code).contains(nextValue)){
                    nextValue = ThreadLocalRandom.current().nextInt(variants);
                }

            code[i] = nextValue;
        }
    }

    public void setCustomCode(int[] code) throws Exception {
        if (code.length == codeLength)
            this.code = code;
        else
            throw new Exception(
                    "Length of code (" + code.length + ") does not match set code length (" + codeLength + ")"
            );
    }

    /**
     *
     * @return A new GuessValidationResult indicating the number of black and white pins
     */
    public GuessValidationResult validateGuess(int[] guess) throws Exception {

        //check if guess is same length as code
        if(guess.length != codeLength){
            throw new Exception(
                    "Length of guess (" + guess.length + ") does not match code length (" + codeLength + ")"
            );
        }

        //set current round
        currentRound++;

        //get number of black and white pins to be returned
        int black = 0;
        int white = 0;

        for(int i = 0; i < codeLength; i++){
            if (guess[i] == code [i])
                black++;
            else if (ArrayUtil.ArrayContainsValue(code, guess[i]))
                white++;
        }

        //check if game is won or lost
        if (black == codeLength)
            gameWon=true;
        else if (currentRound == maxRounds && gameWon == false)
            gameLost=true;

        return new GuessValidationResult(black, white);
    }

}