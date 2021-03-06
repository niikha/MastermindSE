package com.example.mastermind.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.mastermind.Util.ArrayUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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
        long seed = System.nanoTime();
        Random randomSeed = threadRandom(seed).get();
        for (int i = 0; i<codeLength; i++){

            //generate random value between 1 and the number of different colours
            int nextValue = randomSeed.nextInt(variants) + 1;

            //if code already contains value, generate more values until value is unique
            if(!doubleValuesAllowed)
                while (ArrayUtil.ArrayContainsValue(code, nextValue)){
                    nextValue = randomSeed.nextInt(variants);
                }

            code[i] = nextValue;
        }
    }

    public static ThreadLocal<Random> threadRandom(final long seed) {
        return new ThreadLocal<Random>(){
            @Override
            protected Random initialValue() {
                return new Random(seed);
            }
        };
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

        //check for black and white pins
        int black = 0;
        int white = 0;

        int[] cloneOfCode = code.clone();

        for(int i = 0; i < codeLength; i++){
            if (guess[i] == cloneOfCode[i]) {
                black++;
                //Remove value so it can't be counted again
                cloneOfCode[i] = -1;
                guess[i] = -2;
            }
        }

        for(int i = 0; i < codeLength; i++){
            if (ArrayUtil.ArrayContainsValue(cloneOfCode, guess[i])) {
                white++;
                // Find index of 'white' element
                int indexOfWhiteElement = ArrayUtil.IndexOfValue(cloneOfCode, guess[i]);
                cloneOfCode[indexOfWhiteElement] = -1;
            }
        }

        //check if game is won or lost
        if (black == codeLength)
            gameWon = true;
        else if (currentRound == maxRounds && !gameWon)
            gameLost = true;

        return new GuessValidationResult(black, white);
    }

}